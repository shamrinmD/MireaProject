package ru.mirea.shamrin.mireaproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements DrivingSession.DrivingRouteListener, GeoObjectTapListener, InputListener {

    private final Point ROUTE_START_LOCATION = new Point(55.685898, 37.853888);
    private final Point ROUTE_END_LOCATION_1 = new Point(55.794229, 37.700772);
    private final Point ROUTE_END_LOCATION_2 = new Point(55.669845, 37.479265);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION_1.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION_1.getLongitude()) / 2);

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapKitFactory.setApiKey("00c4c68f-1a2d-4f48-8d00-003f6d9e0a87");
        MapKitFactory.initialize(getActivity());
        DirectionsFactory.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapview);
        // Устанавливаем начальную точку и масштаб
        mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0, 0));

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        submitRequest();

        mapView.getMap().addTapListener(this);
        mapView.getMap().addInputListener(this);

        mapObjects.addPlacemark(new Point(55.669945, 37.479492), ImageProvider.fromResource(requireContext(), R.drawable.mark));
        addNewMark("РТУ МИРЭА","14 июля 1900 года" , "г. Москва, Проспект Вернадского, д.86", new Point(55.661963, 37.476537));
        addNewMark("РТУ МИРЭА"," 16 сентября 1936 года","г. Москва, ул. Стромынка, д.20", new Point(55.794433, 37.700560));
        addNewMark("РТУ МИРЭА", "28 мая 1947 года","г. Москва, Проспект Вернадского, д.78", new Point(55.669845, 37.479265));
        addNewMark("Филиал РТУ МИРЭА в г. Ставрополе", "18 декабря 1996 года","Ставропольский край, г. Ставрополь, пр. Кулакова, д.8", new Point(45.050526, 41.910352));
        addNewMark("Филиал РТУ МИРЭА в г. Фрязино", "1962 год","Московская область, г. Фрязино, ул. Вокзальная, д.2а, Корпус 61", new Point(55.965662, 38.048818));
        return view;
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    private void submitRequest() {
        DrivingOptions options = new DrivingOptions();
        // Кол-во альтернативных путей
        options.setAlternativeCount(4);
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        // Устанавливаем точки маршрута
        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION_1, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION_2, RequestPointType.WAYPOINT, null));
        // Делаем запрос к серверу
        drivingSession = drivingRouter.requestRoutes(requestPoints, options, this);
    }
    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "error";
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
        final GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent
                .getGeoObject()
                .getMetadataContainer()
                .getItem(GeoObjectSelectionMetadata.class);

        String text = geoObjectTapEvent.getGeoObject().getName();
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();

        if (selectionMetadata != null) {
            mapView.getMap().selectGeoObject(selectionMetadata.getId(), selectionMetadata.getLayerId());
        }

        return selectionMetadata != null;
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) { mapView.getMap().deselectGeoObject(); }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) { }

    public void addNewMark(String name, String date, String address, Point p){
        PlacemarkMapObject mark = mapObjects.addPlacemark(p, ImageProvider.fromResource(requireContext(), R.drawable.mark));
        mark.setUserData(new University(name, date, address));
        mark.addTapListener((mapObject, point) -> {
            Toast.makeText(requireContext(), (mapObject.getUserData().toString()), Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}