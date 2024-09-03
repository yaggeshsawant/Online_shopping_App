package com.madss.grocery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProductFullDetail extends Fragment  {
    private ViewPager viewPager;
    RecyclerView fullDetailRecyclerViewId;
    private AdapterForProductDetailSlider sliderAdapter;
    Button buyNowId;

    private OnCartCountChangeListener cartCountChangeListener;

    ProgressDialog progressDialog;
    RetrofitApiInterface retrofitApiInterface = null;

    PojoProductInfoSingle newList;
    TextView product_name_id,product_price_id,product_cross_price_id,product_description_id,tvCount, textViewDiscountBadge;

    WebView webView_id;
    String VideoUrl;
    String productId;

    LinearLayout count_linear_layout;
    private int count;
    DBhandler db;

    Button addToCartbtnId;
    String OffPrice;
    AppCompatImageButton btndecrease, btnincrease;

    CardView video_id;

    DashBoardActivity dashBoardActivity;

    @SuppressLint("MissingInflatedId")

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Initialize the cartCountChangeListener
            cartCountChangeListener = (OnCartCountChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCartCountChangeListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_full_detail, container, false);
        Bundle bundle = getArguments();
        productId = bundle.getString("productId");
        initialization(view);





        FullDetailOfProduct(productId);



        addToCartbtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
                notifyCartCountChange();
            }
        });

        btndecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementCount();
                notifyCartCountChange();
            }
        });

        btnincrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementCount();
                notifyCartCountChange();
            }
        });

        buyNowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, new FragmentAddToCart())
                        .addToBackStack(null).commit();
            }
        });


        count = db.getProductCount(productId);
        updateCount();


        // Set initial visibility based on product count
        if (count > 0) {
            addToCartbtnId.setVisibility(View.GONE);
            count_linear_layout.setVisibility(View.VISIBLE);
        } else {
            addToCartbtnId.setVisibility(View.VISIBLE);
            count_linear_layout.setVisibility(View.GONE);
        }

        return view;
    }

    private void notifyCartCountChange() {

            int newCount = db.getAllProducts().size();
            cartCountChangeListener.onCartCountChange(newCount);

    }

    private void AdvertiseVideo() {
        if(VideoUrl!=null) {
            video_id.setVisibility(View.VISIBLE);

            String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/aDq48lX6NjI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";


            // Find the start and end indices of the original src URL
            int srcStartIndex = video.indexOf("src=\"") + 5;
            int srcEndIndex = video.indexOf("\"", srcStartIndex);

            // Replace the src URL with the new VideoURL
            String UpdateVideoURl = video.substring(0, srcStartIndex)
                    + VideoUrl
                    + video.substring(srcEndIndex);
            webView_id.loadData(UpdateVideoURl, "text/html", "utf-8");
            webView_id.getSettings().setJavaScriptEnabled(true);
            webView_id.setWebChromeClient(new WebChromeClient());

        }
    }

    private void addToCart() {

      String  productName = newList.getP_name().toString();
        String  productPrice = newList.price.toString();
        String Imageurl = newList.getImage_one().toString();

        count = 1;
        db.addProduct(productId, productName, productPrice, Imageurl, count,OffPrice);
        updateCount();

        addToCartbtnId.setVisibility(View.GONE);
        count_linear_layout.setVisibility(View.VISIBLE);

    }
    // Method to handle increment button click
    public void incrementCount() {
        String  productName = newList.getP_name().toString();
        String  productPrice = newList.price.toString();
        String Imageurl = newList.getImage_one().toString();

        if (db.isProductExists(productId)) {
            count = db.getProductCount(productId) + 1;
            db.updateProductCount(productId, count);
        } else {
            count = 1;
            db.addProduct(productId, productName, productPrice, Imageurl, count,OffPrice);
        }
        updateCount();

    }

    //         Method to handle decrement button click
    public void decrementCount() {
        if (db.isProductExists(productId)) {
            count = db.getProductCount(productId);
            if (count > 1) {
                count--;
                db.updateProductCount(productId, count);
            } else if (count == 1) {
                count = 0;
                db.updateProductCount(productId, count);
                db.removeProductIfCountZero(productId);
                addToCartbtnId.setVisibility(View.VISIBLE);
                count_linear_layout.setVisibility(View.GONE);
            }
            updateCount();
        }
    }

    //Method to update the TextView with current count
    private void updateCount() {
        tvCount.setText(String.valueOf(count));
    }


    private void FullDetailOfProduct(String productId) {
        progressDialog.show();


        retrofitApiInterface.getProductInfoById(productId).enqueue(new Callback<PojoProductInfoSingle>() {
            @Override
            public void onResponse(Call<PojoProductInfoSingle> call, Response<PojoProductInfoSingle> response) {
                if (response.isSuccessful()) {
                    newList = response.body();
                    VideoUrl=newList.getVideo();
                    product_name_id.setText(newList.getP_name());
                    product_price_id.setText(newList.getFinal_price());
                    OffPrice=newList.getPrice();
                    product_cross_price_id.setText(newList.getPrice());
                    product_description_id.setText(newList.getDiscription());
                    textViewDiscountBadge.setText(newList.getDiscount()+" OFF");



                    HandleViewPager();
                    // Call handleProductImageRecylerView() after newList is populated
                    handleProductImageRecylerView();

                    AdvertiseVideo();

                }
                progressDialog.dismiss();

            }


            @Override
            public void onFailure(Call<PojoProductInfoSingle> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initialization(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        fullDetailRecyclerViewId = view.findViewById(R.id.RecyclerViewId);
        buyNowId = view.findViewById(R.id.buyNowId);
        product_name_id=view.findViewById(R.id.product_name_id);
        product_price_id=view.findViewById(R.id.product_price_id);
        product_cross_price_id=view.findViewById(R.id.mobile_cross_prise_id);
        product_description_id=view.findViewById(R.id.product_description_id);
        textViewDiscountBadge=view.findViewById(R.id.textViewDiscountBadge);
        webView_id=view.findViewById(R.id.webView_id);
        video_id=view.findViewById(R.id.video_id);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout productDetailsLayout = view.findViewById(R.id.product_image_view);
        // Add TextViews, ImageViews, etc., and set the product details
        TextView crossedPrice = view.findViewById(R.id.mobile_cross_prise_id);
        crossedPrice.setPaintFlags(crossedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");

        tvCount=view.findViewById(R.id.tvCount);
        addToCartbtnId = view.findViewById(R.id.addToCartbtnId);
        btndecrease = view.findViewById(R.id.btndecrease);
        btnincrease = view.findViewById(R.id.btnincrease);
        count_linear_layout=view.findViewById(R.id.count_linear_layout);
        db= new DBhandler(getContext());
        dashBoardActivity = (DashBoardActivity) getActivity();

        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);
    }

    public void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void HandleViewPager() {
        // Sample images for the slider
        String[] images = {newList.getImage_one(), newList.getImage_two(), newList.getImage_three()};
        sliderAdapter = new AdapterForProductDetailSlider(requireContext(), images);

        viewPager.setAdapter(sliderAdapter);
    }
    private void handleProductImageRecylerView() {

        List<PojoProductFullDetail> pojoProductFullDetails = new ArrayList<>();

        pojoProductFullDetails.add(new PojoProductFullDetail(newList.getImage_one()));
        pojoProductFullDetails.add(new PojoProductFullDetail(newList.getImage_two()));
        pojoProductFullDetails.add(new PojoProductFullDetail(newList.getImage_three()));


        AdapterForProductFullDetailRecyclerView adapter = new AdapterForProductFullDetailRecyclerView( getActivity(),pojoProductFullDetails,viewPager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false);
        fullDetailRecyclerViewId.setLayoutManager(gridLayoutManager);
        fullDetailRecyclerViewId.setAdapter(adapter);

    }


}