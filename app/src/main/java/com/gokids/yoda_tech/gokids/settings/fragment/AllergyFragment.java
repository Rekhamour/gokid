package com.gokids.yoda_tech.gokids.settings.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokids.settings.adapter.AllergyAdapter;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.utils.GridItemView;
import com.gokids.yoda_tech.gokids.utils.MyGridView;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllergyFragment extends Fragment {

    ArrayList<Allergy> allergies =  new ArrayList<>();
    MyGridView gridView;
    AllergyAdapter allergyAdapter;
    Button allergy_continue;
    private static ArrayList<String> selectedStrings= new ArrayList<>();
    static String TAG = "Allergi fragment";
    private AddKidsActivity activity ;
    private TextView skipallergy;
    private TextView values;
    private boolean _hasLoadedOnce= false;
    private View rootView;


    public static AllergyFragment newInstance(ArrayList<String> list) {
        selectedStrings=list;
        Log.e(TAG," list size"+selectedStrings.toString());

        Bundle args = new Bundle();

        AllergyFragment fragment = new AllergyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

         rootView = inflater.inflate(R.layout.fragment_allergy, container, false);
        allergy_continue = rootView.findViewById(R.id.allergy_continue);
        values = rootView.findViewById(R.id.values);
        skipallergy = rootView.findViewById(R.id.skip_allergy);
        activity=  new AddKidsActivity();
        values.setText("");
        return rootView;
    }

    private void setAlreadyselecteddata() {
        Log.e(TAG," selected not   allergies selected method size"+selectedStrings.size());

        if(selectedStrings.size()>0) {
            Log.e(TAG," if selected list is greater in allergies");

            for (int i = 0; i < selectedStrings.size(); i++) {
                for (int j = 0; j < allergies.size(); j++) {
                    Log.e(TAG," selected not   allergies"+allergies.get(j).isSelected());

                    if (allergies.get(j).getSpecialNeedID().equalsIgnoreCase(selectedStrings.get(i))) {
                        allergies.get(j).setSelected(true);
                        Log.e(TAG," selected alergies"+allergies.get(j).isSelected());
                    }
                }
            }

        }
        else {
          //setupData();

      }
    }

    public void setupGridView(View rootView) {

        gridView = rootView.findViewById(R.id.allergy_gridview);

        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
        gridView.setVerticalScrollBarEnabled(false);


        allergyAdapter = new AllergyAdapter(getContext(), allergies);
        gridView.setAdapter(allergyAdapter);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(allergies.get(position).isSelected())
                {
                    allergies.get(position).setSelected(false);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove(allergies.get(position).getSpecialNeedID());
                }
                else
                {
                    allergies.get(position).setSelected(true);
                    ((GridItemView) v).display(true);
                    selectedStrings.add(allergies.get(position).getSpecialNeedID());
                }
                AddKidsActivity.setAllerygies(selectedStrings);
                for(int j = 0; j< AddKidsActivity.SelectedNeeds.size(); j++)
                {
                    Log.e(TAG," selected strings" + AddKidsActivity.SelectedNeeds.get(j));
                }




            }
        });
    }


           public void setupData() {
               /*Ion.with(getActivity())
                       .load(Urls.BASE_URL+"api/viewSpecialNeedPerCategory/category/SNC1")
                       .asString()
                       .setCallback(new FutureCallback<String>() {
                           @Override
                           public void onCompleted(Exception e, String result) {
                               if(e==null)
                               {
                                   Log.e(TAG," result"+result);
                                   if (result != null) {
                                       try {
                                           Object json = new JSONTokener(result).nextValue();
                                              if(json instanceof JSONArray)
                                              {
                                                  for(int i=0;i<((JSONArray) json).length();i++)
                                                  {


                                                      String Enabled= null;
                                                      try {
                                                          JSONObject obj= ((JSONArray) json).getJSONObject(i);


                                                          String SpecialNeedID= obj.getString("SpecialNeedID").replaceAll("/"," ");
                                                          String SpecialNeed= obj.getString("SpecialNeed").replaceAll("/"," ");
                                                          String ImageURL= obj.getString("ImageURL").replaceAll("/"," ");
                                                          Enabled = obj.getString("Enabled").replaceAll("/"," ");
                                                          if(Enabled.equalsIgnoreCase("1"))
                                                          {
                                                              allergies.add(new Allergy(R.drawable.Imag,true,SpecialNeed,SpecialNeedID));
                                                          }
                                                      } catch (JSONException e1) {
                                                          e1.printStackTrace();
                                                      }


                                                     }
                                                  }
                                              } catch (JSONException e1) {
                                           e1.printStackTrace();
                                       }
                                       catch (Exception e1)
                                       {

                                       }
                                   }

                               }

                           }
                       });*/
               allergies.add(new Allergy(R.drawable.allergy_dairyfree, false, "Dairy Free", "SN26"));
               allergies.add(new Allergy(R.drawable.allergy_eggfree, false, "Egg Free", "SN4"));
               allergies.add(new Allergy(R.drawable.allergy_gluttenfree, false, "Glutten Free", "SN6"));
               allergies.add(new Allergy(R.drawable.allergy_lactosefree, false, "Lactose Free", "SN33"));
               allergies.add(new Allergy(R.drawable.allergy_nuttfree, false, "Nut Free", "SN5"));
               allergies.add(new Allergy(R.drawable.allergy_sesame, false, "Sesame Free", "SN28"));
               allergies.add(new Allergy(R.drawable.allergy_shelfishfree, false, "Shelfish Free", "SN27"));
               allergies.add(new Allergy(R.drawable.allergy_soyfree, false, "Soy Free", "SN29"));
               allergies.add(new Allergy(R.drawable.allergy_wheatfree, false, "Wheat Free", "SN32"));
           }


           public void setListView() {

               ViewGroup vg = gridView;
               int totalHeight = 0;
               for (int i = 0; i < allergyAdapter.getCount(); i++) {
                   View listItem = allergyAdapter.getView(i, null, vg);
                   listItem.measure(0, 0);
                   totalHeight += listItem.getMeasuredHeight();
               }

               ViewGroup.LayoutParams par = gridView.getLayoutParams();
               par.height = totalHeight / 3;
               gridView.setLayoutParams(par);
               gridView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

               });
               gridView.requestLayout();

           }

           public void continueButton() {

               allergy_continue.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (AddKidsActivity.mViewPager.getCurrentItem() != 5) {
                           AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);
                       }
                   }
               });
               skipallergy.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (AddKidsActivity.mViewPager.getCurrentItem() != 5) {
                           AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);
                       }
                   }
               });
           }


           public class MultiChoiceModeListener implements
                   GridView.MultiChoiceModeListener {
               public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                   mode.setTitle("Select Items");
                   mode.setSubtitle("One item selected");
                   return true;
               }

               public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                   return true;
               }

               public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                   return true;
               }

               public void onDestroyActionMode(ActionMode mode) {
               }

               public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                     long id, boolean checked) {
                   int selectCount = gridView.getCheckedItemCount();
                   switch (selectCount) {
                       case 1:
                           mode.setSubtitle("One item selected");
                           break;
                       default:
                           mode.setSubtitle("" + selectCount + " items selected");
                           break;
                   }
               }

           }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                setupData();
                setAlreadyselecteddata();
                setupGridView(rootView);
                setListView();
                continueButton();
                _hasLoadedOnce = true;
            }
        }
    }

       }


