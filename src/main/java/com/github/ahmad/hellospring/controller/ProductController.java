package com.github.ahmad.hellospring.controller;


import com.github.ahmad.hellospring.bean.ApiError;
import com.github.ahmad.hellospring.bean.ApiResponse;
import com.github.ahmad.hellospring.bean.RestrictByRole;
import com.github.ahmad.hellospring.domain.Product;
import com.github.ahmad.hellospring.service.ProductServices;
import com.google.gson.JsonObject;
import com.kastkode.springsandwich.filter.annotation.Before;
import com.kastkode.springsandwich.filter.annotation.BeforeElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

/*@Before(
        @BeforeElement(value = RestrictByRole.class))*/
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final ProductServices productServices;

    @Autowired
    public ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }

    @RequestMapping(
            value = "/index",
            method = RequestMethod.GET

    )
    @ResponseBody
    public ResponseEntity<ApiResponse> index() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .message("index page")
                        .build());
    }

    @RequestMapping(
            value = "/store",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse> store(@RequestBody Product request) {
        if (request.getName().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .message("Error Validation")
                            .data(request)
                            .error(ApiError.builder()
                                    .code(20)
                                    .message("Field nama kosong")
                                    .field("nama")
                                    .build())
                            .status(HttpStatus.FORBIDDEN)
                            .build());
        } else if (request.getTitle().equals("") || request.getTitle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .message("Error Validation")
                            .data(request)
                            .error(ApiError.builder()
                                    .code(20)
                                    .message("Field title kosong")
                                    .field("title")
                                    .build())
                            .status(HttpStatus.FORBIDDEN)
                            .build());

        } else if (request.getDescription().equals("") || request.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .message("Error Validation")
                            .data(request)
                            .error(ApiError.builder()
                                    .code(20)
                                    .message("Field deskription kosong")
                                    .field("title")
                                    .build())
                            .status(HttpStatus.FORBIDDEN)
                            .build());

        } else if (request.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .message("Error Validation")
                            .data(request)
                            .error(ApiError.builder()
                                    .code(20)
                                    .message("Field price kosong")
                                    .field("price")
                                    .build())
                            .status(HttpStatus.FORBIDDEN)
                            .build());

        } else if (request.getExpiredDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .message("Error Validation")
                            .data(request)
                            .error(ApiError.builder()
                                    .code(20)
                                    .message("Field expired date kosong")
                                    .field("expiredDate")
                                    .build())
                            .status(HttpStatus.FORBIDDEN)
                            .build());

        } else {
            Product product = productServices.saveProduct(request);

            PNConfiguration pnConfiguration = new PNConfiguration();
            pnConfiguration.setSubscribeKey("sub-c-a67e0618-7e7c-11e8-9052-ea2b46234d76");
            pnConfiguration.setPublishKey("pub-c-fc03b82a-256d-439c-9dfa-5a28c7684efb");
            pnConfiguration.setSecure(false);

            PubNub pubnub = new PubNub(pnConfiguration);
            Long dataCount = productServices.findCountV2(request.getTitle());
            Integer dataTerjual = productServices.findCountStatusTerjualV2(request.getStatus());
            Integer dataBelumTerjual = productServices.findCountStatusBelumTerjualV2(request.getStatus());
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("banyak_product", dataCount);
            jsonObject.addProperty("product_terjual", dataTerjual);
            jsonObject.addProperty("product_belum_terjual", dataBelumTerjual);
            JsonObject jsonRoot = new JsonObject();
            //jsonRoot.add("eon",jsonObject);
            System.out.println("Data" + jsonObject.toString());
            pubnub.publish()
                    .message(jsonObject.toString())
                    .channel("test")
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult pnPublishResult, PNStatus pnStatus) {
                            if (!pnStatus.isError()) {
                                System.out.println("pub timetoken: " + pnPublishResult.getTimetoken());
                            }
                            System.out.println("pub status code: " + pnStatus.getStatusCode());
                        }
                    });
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse
                            .builder()
                            .code(201)
                            .status(HttpStatus.CREATED)
                            .message("Data berhasil disimpan")
                            .data(product)
                            .build()
                    );
        }
    }

/*
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.FORBIDDEN);
    }
*/

    @RequestMapping(
            value = "/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse> products() {
        Iterable<Product> product = productServices.findAll();
        if (product.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.builder()
                            .code(200)
                            .status(HttpStatus.OK)
                            .message("Data ditemukan")
                            .data(product)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .message("Data tidak ditemukan")
                            .data(null)
                            .build());
        }
    }

    @RequestMapping(
            value = "/delete/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
        productServices.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .message("Data berhasil dihapus")
                        .build()
                );
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateById(@PathVariable("id") Long id, @RequestBody Product product) {
        Product get = productServices.findOne(id);
        get.setDescription(product.getDescription());
        get.setName(product.getName());
        get.setPrice(product.getPrice());
        get.setTitle(product.getTitle());
        get.setExpiredDate(product.getExpiredDate());
        Product result = productServices.saveProduct(get);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .data(result)
                        .code(200)
                        .message("Data berhasil diupdate")
                        .build()
                );

    }
}
