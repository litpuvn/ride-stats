//package edu.ttu.spm.cheapride.model;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.maps.android.clustering.Cluster;
//import com.google.maps.android.clustering.ClusterManager;
//import com.google.maps.android.clustering.view.DefaultClusterRenderer;
//import com.google.maps.android.ui.IconGenerator;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import edu.ttu.spm.cheapride.MainActivity;
//import edu.ttu.spm.cheapride.R;
//import edu.ttu.spm.cheapride.model.item.Asset;
//import edu.ttu.spm.cheapride.model.item.clusterItem;
//
//import static edu.ttu.spm.cheapride.R.id.ad_image_view;
//import static edu.ttu.spm.cheapride.R.id.image;
//
///**
// * Created by Administrator on 2017/5/30.
// */
//
//public class ClusteringDemoActivity extends MainActivity implements ClusterManager.OnClusterClickListener<Asset>, ClusterManager.OnClusterInfoWindowClickListener<Asset>, ClusterManager.OnClusterItemClickListener<Asset>, ClusterManager.OnClusterItemInfoWindowClickListener<Asset> {
//    private ClusterManager<Asset> mClusterManager;
//    private Random mRandom = new Random(1984);
//
//    /**
//     * Draws Rose Chart inside markers (using IconGenerator).
//     * When there are multiple Chart in the cluster, draw multiple Chart (using MultiDrawable).
//     */
//    public class AssetRenderer extends DefaultClusterRenderer<Asset> {
//        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
//        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
//        //private final ImageView mImageView;
//        private final ImageView mClusterImageView;
//        //private final int mDimension;
//
//        public AssetRenderer() {
//            super(getApplicationContext(), getMap(), mClusterManager);
//
//            View multiProfile = (ImageView)findViewById(R.id.ad_image_view);
//            mClusterIconGenerator.setContentView(multiProfile);
//            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
//        }
//
//        @Override
//        protected void onBeforeClusterItemRendered(Asset Asset, MarkerOptions markerOptions) {
//            // Draw a single Asset.
//            // Set the info window to show their name.
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(ad_image_view)).title("test1");
//        }
//
//        @Override
//        protected void onBeforeClusterRendered(Cluster<Asset> cluster, MarkerOptions markerOptions) {
//            // Draw a single Asset.
//            // Set the info window to show their name.
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(ad_image_view)).title("test2");
//        }
//
//        @Override
//        protected boolean shouldRenderAsCluster(Cluster cluster) {
//            // Always render clusters.
//            return cluster.getSize() > 1;
//        }
//
//    }
//    @Override
//    public boolean onClusterClick(Cluster<Asset> cluster) {
//        return false;
//    }
//
//    @Override
//    public void onClusterInfoWindowClick(Cluster<Asset> cluster) {
//
//    }
//
//    @Override
//    public boolean onClusterItemClick(Asset asset) {
//        return false;
//    }
//
//    @Override
//    public void onClusterItemInfoWindowClick(Asset asset) {
//
//    }
//
//    protected void startDemo() {
//        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.988612, 53.459411), 4.5f));
//
//        mClusterManager = new ClusterManager<>(this, getMap());
//        mClusterManager.setRenderer(new AssetRenderer());
//        getMap().setOnCameraIdleListener(mClusterManager);
//        getMap().setOnMarkerClickListener(mClusterManager);
//        getMap().setOnInfoWindowClickListener(mClusterManager);
//        mClusterManager.setOnClusterClickListener(this);
//        mClusterManager.setOnClusterInfoWindowClickListener(this);
//        mClusterManager.setOnClusterItemClickListener(this);
//        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
//
//        addItems();
//        mClusterManager.cluster();
//    }
//
//        private void addItems() {
//
//            for (int i = 0; i < 300; i++) {
//                mClusterManager.addItem(new Asset(position(), randomStatus()));
//            }
//        }
//    private LatLng position() {
//        return new LatLng(random(30.325877, 36.615905), random(60.648976, 47.465382));
//    }
//    private double random(double min, double max) {
//        return mRandom.nextDouble() * (max - min) + min;
//    }
//    private String randomStatus() {
//        return "Test";
//    }
//
//}
