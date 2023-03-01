package com.android.jsjstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.jsjstore.helper.CartManagement;
import com.android.jsjstore.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView addToCartBtn;
    private TextView txtTitle, txtFee, txtDescription, txtNumberOrdered, txtTotalPrice, txtRanking;
    private ImageView plusBtn, minusBtn, productPicture;
    private Product productDomain;
    private int numberOrder = 1;
    private CartManagement cartManagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

//        cartManagment = new CartManagement(this);

        initView();
        getBundle();
        bottonNavigation();
    }

    private void bottonNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout checkOut = findViewById(R.id.checkoutBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
            }
        });

//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
//            }
//        });

//        checkOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ProductDetailActivity.this, CheckoutActivity.class));
//            }
//        });
    }

    private void getBundle() {
//        productDomain = (ProductDomain)getIntent().getSerializableExtra("object");
//
//        int drawableResourceId = this.getResources().getIdentifier(productDomain.getMainPicture(), "drawable", this.getPackageName());
//        productPicture.setImageResource(drawableResourceId);
//        txtTitle.setText(productDomain.getTitle());
//        txtFee.setText( "$"+productDomain.getPrice());
//        txtDescription.setText(productDomain.getDescription());
//        txtNumberOrdered.setText(String.valueOf(numberOrder));
//        txtRanking.setText(String.valueOf(productDomain.getRank()));
//
//        txtTotalPrice.setText("Total: $" +String.valueOf((double) Math.round(numberOrder*productDomain.getPrice() * 100 ) / 100));
//
//        plusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                numberOrder = numberOrder +1;
//                txtNumberOrdered.setText(String.valueOf(numberOrder));
//                txtTotalPrice.setText("Total: $" +String.valueOf((double) Math.round(numberOrder*productDomain.getPrice() * 100 ) / 100));
//            }
//        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOrder > 1){
                    numberOrder = numberOrder-1;
                }
                txtNumberOrdered.setText(String.valueOf(numberOrder));
                txtTotalPrice.setText("Total: $" +String.valueOf((double) Math.round(numberOrder*productDomain.getPrice() * 100 ) / 100));
            }
        });

//        addToCartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                productDomain.setNumberInCart(numberOrder);
//
//                cartManagment.InsertProduct(new CartDomain(
//                        productDomain.getTitle(),
//                        productDomain.getNumberInCart(),
//                        productDomain.getPrice(),
//                        productDomain.getPicture()
//                ));
//            }
//        });
    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCardBtn);
        txtTitle = findViewById(R.id.txtProductDetailTitle);
        txtFee = findViewById(R.id.txtProductDetailTitle);
        txtDescription = findViewById(R.id.txtProductDetailDescription);
        txtNumberOrdered = findViewById(R.id.txtNumberItem);
        plusBtn = findViewById(R.id.plusCardBtn);
        minusBtn = findViewById(R.id.minusCardBtn);
        productPicture = findViewById(R.id.productDetailImage);
        txtTotalPrice = findViewById(R.id.txtDetailPrice);
        txtRanking = findViewById(R.id.txtDetailRank);
    }
}