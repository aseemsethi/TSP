package in.netstack.tsp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

/**
 * Created by aseem on 18-10-2016.
 */

public class startTSP extends Fragment {
    private static final String TAG = "TSP Start";
    int xVal, yVal;
    TextView calcT;
    int MAXCITY = 20;
    int numCities = 0;
    int POP_SIZE=20;
    Trip[] TripInstance = new Trip[POP_SIZE];
    boolean running = false;
    int instance = 0;
    View aView;

    // This is the city size
    class Cities {
        public int xVal, yVal;
        public String name;
    }
    Cities[] myCities = new Cities[MAXCITY];

    // This is a Trip
    class Trip {
        ArrayList trip = new ArrayList<Cities>();
        double cost = 0;
        double fitness = 0;

        public Trip() {
            for (int i = 0; i < numCities; i++) {
                trip.add(null);
            }
        }
        // Creates a random trip
        public void generateTrip() {
            // Loop through all our destination cities and add them to our tour
            for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
                trip.set(cityIndex, myCities[cityIndex]);
            }
            // Randomly reorder the tour
            Collections.shuffle(trip);
        }
        public void showCities() {
            for (int i = 0; i < numCities; i++) {
                Cities city = (Cities)trip.get(i);
                calcT.append("\nCity: " + city.name +
                        "  X: " + city.xVal + " Y: " + city.yVal);
            }
        }
        public void showTrip() {
            Log.d(TAG, "show trip");
            calcT.append("\nTour: ");
            for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
                calcT.append(((Cities)trip.get(cityIndex)).name);
            }
        }
        public void showDistance(boolean op) {
            if (cost == 0) {
                for (int cityIndex = 0; cityIndex < numCities - 1; cityIndex++) {
                    cost += distance(((Cities) trip.get(cityIndex)), ((Cities) trip.get(cityIndex + 1)));
                }
            }
            if (op == true)
                calcT.append(" Cost: " + Math.round(cost));
        }
        public double getFitness(boolean op) {
            if (cost == 0)    showDistance(false);
            if (fitness != 0) {
                Log.d(TAG, "Trip Fitness.: " + fitness);
                if (op == true)
                    calcT.append(" Fitness" + fitness);
                return fitness;
            }
            fitness = 1/(double)Math.round(cost);
            Double truncatedDouble = BigDecimal.valueOf(fitness)
                    .setScale(5, RoundingMode.HALF_UP)
                    .doubleValue();
            fitness = truncatedDouble;
            Log.d(TAG, "Trip Fitness: " + fitness);
            if (op == true)
                calcT.append(" Fitness" + fitness);
            return (fitness);
        }
    }

    private class Circle extends View{
        Paint paint = new Paint();
        public Circle (Context context) {
            super(context);
            this.setWillNotDraw(false);
        }
        @Override
        public void onDraw(Canvas canvas) {
            Log.d(TAG, "onDraw called...");
            putCities(canvas, calcT, TripInstance[instance]);
        }
    }

    public double distance(Cities city1, Cities city2) {
        int xDistance = Math.abs(city1.xVal - city2.xVal);
        int yDistance = Math.abs(city1.yVal - city2.yVal);
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );

        return distance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.start_fragment, container, false);
        //Initialize the Array
        for (int i=0;i<MAXCITY;i++) {
            myCities[i] = new Cities();
            myCities[i].name = new String("");
        }

        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.frame);
        aView = new Circle(getActivity());
        relativeLayout.addView(aView);

        final RelativeLayout fl = (RelativeLayout) v.findViewById(R.id.frame);
        Button addCity = (Button) v.findViewById(R.id.add_city);
        final TextView xPtr = (TextView) v.findViewById(R.id.x);
        final TextView yPtr = (TextView) v.findViewById(R.id.y);
        Button run = (Button) v.findViewById(R.id.run);
        Button stop = (Button) v.findViewById(R.id.stop);
        Button clear = (Button) v.findViewById(R.id.clear);
        calcT = (TextView) v.findViewById(R.id.calc);

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "starting TSP");
                Toast.makeText(v.getContext(), "Add City", Toast.LENGTH_SHORT).show();
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
                Log.d(TAG, "running TSP");
                runAlgo();

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                numCities = 0;
                calcT.append("\nTest Completed....");
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                numCities = 0;
                Log.d(TAG, "clearing TSP");
                Toast.makeText(v.getContext(), "Clear Cities", Toast.LENGTH_SHORT).show();
                xPtr.setText("X-Axis");
                yPtr.setText("Y-Axis");
                aView.invalidate();
            }
        });
        fl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (running == true) {
                        Toast.makeText(getActivity(), "Program in progress..click Stop to end",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    if (numCities >= MAXCITY) {
                        Toast.makeText(getActivity(), "You have selected 20 cities",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    xVal = Math.round(event.getX());
                    yVal = Math.round(event.getY());
                    myCities[numCities].xVal = xVal;
                    myCities[numCities].yVal = yVal;
                    xPtr.setText(String.valueOf(xVal));
                    yPtr.setText(String.valueOf(yVal));
                    Log.d(TAG, "x: " + String.valueOf(event.getX()) + "  y:" + String.valueOf(event.getY()));
                    final int currentNum = numCities;
                    final EditText txtUrl = new EditText(getActivity());
                    txtUrl.setHint("Enter city name");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Enter City")
                            .setMessage("Name:")
                            .setView(txtUrl)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    hideKeyboard(v.getContext());
                                    String cityName = txtUrl.getText().toString();
                                    myCities[currentNum].name = cityName;
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                    aView.invalidate();
                    numCities++;
                }
                return true;
            }
        });
        return v;
    }
    public void runAlgo() {
        // Generate a number of trips - currently 20
        for (int i=0;i<POP_SIZE;i++) {
            TripInstance[i] = new Trip();
            TripInstance[i].generateTrip();
        }
        TripInstance[0].showCities();

        // Get min trip size
        int minIndex = 0;
        for(int i=0;i<POP_SIZE;i++) {
            if (TripInstance[minIndex].getFitness(false) < TripInstance[i].getFitness(false)) {
                minIndex = i;
                Log.d(TAG, "\n" + i + ": "+ String.valueOf(TripInstance[i].getFitness(false)));
            }
        }
        calcT.append("\nBest Starting Trip: ");
        TripInstance[minIndex].showTrip();
        TripInstance[minIndex].showDistance(true);
        TripInstance[minIndex].getFitness(true);
        instance = minIndex;
        aView.invalidate();

        for (int j=0;j<100;j++) {
            // Do Crossover within the same population of trips
            Trip[] TripInstanceNew = new Trip[POP_SIZE];
            for (int i=0;i<POP_SIZE;i++) {
                TripInstanceNew[i] = new Trip();
                TripInstanceNew[i] = TripInstance[i];
            }
            /* TBD */
            // From 1st 5 entries (these are mixed, so are randomly distributed,
            // take out top 2 fittest trips and do a crossover
            //Trip fittest = getFittestTrip(TripInstanceNew);
            // This works, since getFittest makes the fittest entry null.
            //Trip nextFittest = getFittestTrip(TripInstanceNew);
            //crossover (fittest, nextFittest)
            /*
            int x = randomWithRange(1,5);
            for (int i=0; i<x;i++) {
                fittest.trip.set(i, nextFittest.trip.get(i));
            }
            for (int i=x;i<fittest.trip.size();i++) {
                fittest.trip.set(i, nextFittest.trip.get(i));
            }
            */
            // Do some mutation

            // Loop again
        }
    }
    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    public void putCities(Canvas canvas, TextView calcT, Trip TripInstance) {
        Paint paint=new Paint();
        paint.setTextSize(50);
        //calcT.setText("Cities:");
        for (int i=0;i<numCities;i++) {
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(myCities[i].xVal, myCities[i].yVal, 20, paint);
            paint.setColor(Color.parseColor("#0000ff"));
            canvas.drawText(myCities[i].name, myCities[i].xVal+15, myCities[i].yVal+15, paint);
            //calcT.append("\nCity: " + myCities[i].name +
                   // " X: " + myCities[i].xVal +  " Y: " + myCities[i].yVal);
        }
        if (TripInstance != null) {
            for (int i = 0; i < numCities - 1; i++) {
                Cities toCity, fromCity;
                fromCity = ((Cities)(TripInstance.trip.get(i)));
                toCity = ((Cities)(TripInstance.trip.get(i+1)));
                /* canvas.drawLine(myCities[i].xVal, myCities[i].yVal,
                        myCities[i + 1].xVal, myCities[i + 1].yVal, paint); */
                canvas.drawLine(toCity.xVal, toCity.yVal,
                        fromCity.xVal, fromCity.yVal, paint);
            }
            //TripInstance.showTrip();
            //TripInstance.showDistance();
        } else {
            Log.d(TAG, "Tripinstance is null");
        }
    }
    public static void hideKeyboard(Context ctx) {
            InputMethodManager inputManager = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // check if no view has focus:
            View v = ((Activity) ctx).getCurrentFocus();
            if (v == null)
                return;
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return;
    }

}