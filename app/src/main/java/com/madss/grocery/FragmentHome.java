package com.madss.grocery;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.chayanforyou.slider.SliderLayout;
import io.github.chayanforyou.slider.animations.DescriptionAnimation;
import io.github.chayanforyou.slider.slidertypes.BaseSliderView;
import io.github.chayanforyou.slider.slidertypes.TextSliderView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {


    // array of images, and handler for auto-slider
    Context context = null;

    View view;
    SliderLayout mDemoSlider,FrontAddSlider;
    ProgressDialog progressDialog;
    RecyclerView categoryRecyclerViewId, frontCategoryRecyclerId, newProductRecyclerId, stuFeedbackRecycleId;

    private TextView newsTextView;
    RetrofitApiInterface retrofitApiInterface = null;




    // Override the onCreateView method to inflate the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = view;
        // handle all initialization
        initialization();
        // Handle marquee setup
        handleMarquee();
        // Handle Slider setup
        handleSlider();
        handlerFrontAdvertisementSlider();
        handleFrontCategoryRecyclerView();
        handlecategoryRecyclerViewId();
        handlenewproduct();

        // handleStuFeedbackRecycleView();
        // Set back button press listener
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back press within the fragment
                CustomDialogExit.showDialog(context);
            }
        });


        return view;
    }

    private void handlerFrontAdvertisementSlider() {
        retrofitApiInterface.getFrontAdd().enqueue(new Callback<List<PojoAdd>>() {
            @Override
            public void onResponse(Call<List<PojoAdd>> call, Response<List<PojoAdd>> response) {
                if (response.isSuccessful()) {
                    List<PojoAdd> newList= response.body();
                    Log.d("API_Response", "Response Message: " + response.body());

                    if (newList != null && newList.size() > 0) {
                        for (int i = 0; i < newList.size(); i++) {
                            PojoAdd dto = newList.get(i);

                            String imageUrl = /*RetrofitApiClient.getBaseUrl() + "/./SliderImages/" + dto.getId() + "." +*/ dto.getImage_formate();

                            TextSliderView sliderView = new TextSliderView(getActivity());

                            // Initialize a SliderLayout
                            sliderView/*.description(dto.getName())*/
                                    .image(imageUrl)
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            // Add SliderView to the SliderLayout
                            FrontAddSlider.addSlider(sliderView);
                        }
                    }
                } else {
                    setDefaultSlider();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoAdd>> call, Throwable t) {
                // Handle failure scenario if needed
                setDefaultSlider();
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });
        FrontAddSlider.setDuration(4000);
        FrontAddSlider.setCustomAnimation(new DescriptionAnimation());
        FrontAddSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        FrontAddSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

    }

    private void handlenewproduct() {

        retrofitApiInterface.getLatestproduct().enqueue(new Callback<List<PojoProductInfoSingle>>() {
            @Override
            public void onResponse(Call<List<PojoProductInfoSingle>> call, Response<List<PojoProductInfoSingle>> response) {
                if (response.isSuccessful()) {
                    List<PojoProductInfoSingle> newList = response.body();

                    RecyclerViewAdpater2 recyclerViewAdpater2=new RecyclerViewAdpater2(newList,getActivity());
                    GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                    newProductRecyclerId.setLayoutManager(gridLayoutManager2);
                    newProductRecyclerId.setAdapter(recyclerViewAdpater2);


                }
            }

            @Override
            public void onFailure(Call<List<PojoProductInfoSingle>> call, Throwable t) {

            }
        });




    }



    public void initialization() {
        context = getActivity();

        newsTextView = view.findViewById(R.id.news);
        mDemoSlider = view.findViewById(R.id.slider);
        FrontAddSlider= view.findViewById(R.id.FrontAdvertisementSlider);


        frontCategoryRecyclerId = view.findViewById(R.id.frontCategoryRecyclerId);
        categoryRecyclerViewId = view.findViewById(R.id.categoryRecyclerViewId);
        newProductRecyclerId = view.findViewById(R.id.newProductRecyclerId);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");


        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

    }

    ;

    /*private void handleMarquee() {
        //progressDialog.show();
        retrofitApiInterface.getAllNewsInfo().enqueue(new Callback<List<PojoNews>>() {
            @Override
            public void onResponse(Call<List<PojoNews>> call, Response<List<PojoNews>> response) {
                if (response.isSuccessful()) {
                    List<PojoNews> newList = response.body();
                    Log.d("API_Response", "Response Message: " + response.body());
                    if (newList != null && newList.size() > 0) {
                        String finalMessage = "";
                        for (int i = 0; i < newList.size(); i++) {
                            PojoNews dto = newList.get(i);

                            finalMessage += " ||   * " + dto.getName() + ".";

                            // Set click listener for the TextView
                            *//*newsTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Open link for the current message
                                    openLink();
                                }
                            });*//*
                        }

                        // Set initial text
                        newsTextView.setText(finalMessage);
                        newsTextView.setTypeface(null);
                        newsTextView.setSelected(true);
                    }

                } else {


                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoNews>> call, Throwable t) {
                // Set initial text
                newsTextView.setText("This is Default Message");
                newsTextView.setTypeface(null);
                newsTextView.setSelected(true);

                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        });
    }*/


    private void handleSlider() {
        retrofitApiInterface.getAllSliderInfo().enqueue(new Callback<List<PojoSlider>>() {
            @Override
            public void onResponse(Call<List<PojoSlider>> call, Response<List<PojoSlider>> response) {
                if (response.isSuccessful()) {
                    List<PojoSlider> newList = response.body();
                    Log.d("API_Response", "Response Message: " + response.body());

                    if (newList != null && newList.size() > 0) {
                        for (int i = 0; i < newList.size(); i++) {
                            PojoSlider dto = newList.get(i);

                            String imageUrl = /*RetrofitApiClient.getBaseUrl() + "/./SliderImages/" + dto.getId() + "." +*/ dto.getImage_formate();

                            TextSliderView sliderView = new TextSliderView(getActivity());

                            // Initialize a SliderLayout
                            sliderView/*.description(dto.getName())*/
                                    .image(imageUrl)
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            // Add SliderView to the SliderLayout
                            mDemoSlider.addSlider(sliderView);
                        }
                    }
                } else {
                    setDefaultSlider();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoSlider>> call, Throwable t) {
                // Handle failure scenario if needed
                setDefaultSlider();
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }




    // Method to open fragments
    private void openFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_drawer_id, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    public void setDefaultSlider() {
        // Handle unsuccessful response
        for (int i = 0; i < 4; i++) {
            TextSliderView sliderView = new TextSliderView(getActivity());

            // Initialize a SliderLayout with default image
            sliderView/*.description("Default")*/
                    .image(R.drawable.img_no_image)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            // Add SliderView to the SliderLayout
            mDemoSlider.addSlider(sliderView);
        }


    }
    private void handleMarquee() {

        retrofitApiInterface.getAllNewsInfo().enqueue(new Callback<List<PojoNews>>() {
            @Override
            public void onResponse(Call<List<PojoNews>> call, Response<List<PojoNews>> response) {
                if (response.isSuccessful()) {
                    List<PojoNews> newList = response.body();
                    Log.d("API_Response", "Response Message: " + response.body());
                    if (newList != null && newList.size() > 0) {
                        String finalMessage = "";
                        for (int i = 0; i < newList.size(); i++) {
                            PojoNews list = newList.get(i);

                            finalMessage += " ||   * " + list.getName() + "*  || ";

                            // Set click listener for the TextView
                            /*newsTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Open link for the current message
                                    openLink();
                                }
                            });*/
                        }

                        // Set initial text
                        newsTextView.setText(finalMessage);
                        newsTextView.setTypeface(null);
                        newsTextView.setSelected(true);
                    }

                } else {


                }
            }

            @Override
            public void onFailure(Call<List<PojoNews>> call, Throwable t) {
                // Set initial text
                newsTextView.setText("This is Default Message");
                newsTextView.setTypeface(null);
                newsTextView.setSelected(true);

                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void handlecategoryRecyclerViewId() {

        retrofitApiInterface.getAllCategory().enqueue(new Callback<List<PojoCategory>>() {
            @Override
            public void onResponse(Call<List<PojoCategory>> call, Response<List<PojoCategory>> response) {
                if (response.isSuccessful()) {
                    List<PojoCategory> newList = response.body();

                    AdapterForCategory adapter = new AdapterForCategory(newList, getActivity());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false);
                    categoryRecyclerViewId.setLayoutManager(gridLayoutManager);
                    categoryRecyclerViewId.setAdapter(adapter);


                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoCategory>> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING SLIDER", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void handleFrontCategoryRecyclerView() {
        retrofitApiInterface.getFrontCat().enqueue(new Callback<List<PojoCategory>>() {
        @Override
        public void onResponse(Call<List<PojoCategory>> call, Response<List<PojoCategory>> response) {
            if (response.isSuccessful()) {
                List<PojoCategory> newList = response.body();

                AdapterForFrontCat adapterForFrontCat =new AdapterForFrontCat(newList,getActivity());
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                frontCategoryRecyclerId.setLayoutManager(linearLayoutManager);
                frontCategoryRecyclerId.setAdapter(adapterForFrontCat);


            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<List<PojoCategory>> call, Throwable t) {
            // Handle failure scenario if needed
            progressDialog.dismiss();
            Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
            Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING SLIDER", Toast.LENGTH_SHORT).show();

        }
    });

    }
/*
    private List<MyModel>  AddData() {
        List<MyModel> data = new ArrayList<>();
        data.add(new MyModel("samsung",R.drawable.no_img));
        data.add(new MyModel("vivo",R.drawable.no_img));
        data.add(new MyModel("redmi",R.drawable.no_img));
        data.add(new MyModel("one-plus",R.drawable.no_img));
        data.add(new MyModel("realme",R.drawable.no_img));
        data.add(new MyModel("oppo",R.drawable.no_img));


        return  data;
    }*/

//    private List<MyModel2> AddData2() {
//        List<MyModel2> data = new ArrayList<>();
//        data.add(new MyModel2("vivo",R.drawable.no_img));
//        data.add(new MyModel2("vivo",R.drawable.no_img));
//
//
//        return  data;
//    }
/*    class MyModel {

        private String text;
        private int Imageview;

        public MyModel(String text, int Imageview) {
            this.text = text;
            this.Imageview=Imageview;
        }

        public String getText() {
            return text;
        }

        public int getImageview(){return Imageview; }


    }
    class MyModel2 {

        private String text;
        private int Imageview;



        public MyModel2(String text, int Imageview) {
            this.text = text;

            this.Imageview=Imageview;
        }


        public String getText() {
            return text;
        }

        public int getImageview(){return Imageview; }


    }*/
}
