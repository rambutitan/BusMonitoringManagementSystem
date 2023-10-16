package com.example.busmonitoringandticketingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private List<BusData> busUserList;

    public BusAdapter(List<BusData> busUserList) {
        this.busUserList = busUserList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BusData busUser = busUserList.get(position);
        holder.availabilityView.setText(busUser.getAvailabilityView());
        holder.dateEditedView.setText(busUser.getDateEditedView());
        holder.driverView.setText(busUser.getDriverView());
        holder.driverNumberView.setText(busUser.getDriverNumberView());
        holder.onUCLMView.setText(busUser.getOnUCLMView());
        holder.routeView.setText(busUser.getRouteView());
        /*
        if(busUser.getSchedule_1230_pmView() == ""){
            holder.schedule_1230_pmView.setText(" ");
            }
        else {
            holder.schedule_1230_pmView.setText(busUser.getSchedule_1230_pmView());
        }

        if(busUser.getSchedule_600_amView()== ""){

            holder.schedule_600_amView.setText(busUser.getSchedule_600_amView());
        }

        else {
            holder.schedule_600_amView.setText(" ");
        }

        if(busUser.getSchedule_330_pmView() == ""){

            holder.schedule_330_pmView.setText(" ");
        }
        else {
            holder.schedule_330_pmView.setText(busUser.getSchedule_330_pmView());

        }
        if(busUser.getSchedule_600_pmView() == ""){

            holder.schedule_600_pmView.setText(" ");
         }
        else {
            holder.schedule_600_pmView.setText(busUser.getSchedule_600_pmView());

        }

        if(busUser.getSchedule_900_pmView() == ""){

            holder.schedule_900_pmView.setText(" ");
        }
        else {
            holder.schedule_900_pmView.setText(busUser.getSchedule_900_pmView());

        }
*/
        holder.schedule_1230_pmView.setText(busUser.getSchedule_1230_pmView());
        holder.schedule_330_pmView.setText(busUser.getSchedule_330_pmView());
        holder.schedule_600_amView.setText(busUser.getSchedule_600_amView());
        holder.schedule_600_pmView.setText(busUser.getSchedule_600_pmView());
        holder.schedule_900_pmView.setText(busUser.getSchedule_900_pmView());
        holder.timeEditedView.setText(busUser.getTimeEditedView());

        /*
 */
    }

    @Override
    public int getItemCount() {
        return busUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateEditedView;
        public TextView timeEditedView;
        public TextView routeView;
        public TextView driverView;
        public TextView driverNumberView;
        public TextView schedule_600_amView;
        public TextView schedule_1230_pmView;
        public TextView schedule_330_pmView;
        public TextView schedule_600_pmView;
        public TextView schedule_900_pmView;
        public TextView onUCLMView;
        public TextView availabilityView;
        //public TextView dateIDView;

        public ViewHolder(View itemView) {
            super(itemView);

            availabilityView = itemView.findViewById(R.id.availabilityText);
            dateEditedView = itemView.findViewById(R.id.dateText);
            timeEditedView = itemView.findViewById(R.id.timeText);
            routeView = itemView.findViewById(R.id.routeText);
            driverView = itemView.findViewById(R.id.driverText);
            driverNumberView = itemView.findViewById(R.id.driver_numberText);
            schedule_600_amView = itemView.findViewById(R.id.schedule_600_amText);
            schedule_1230_pmView = itemView.findViewById(R.id.schedule_1230_pmText);
            schedule_330_pmView = itemView.findViewById(R.id.schedule_330_pmText);
            schedule_600_pmView = itemView.findViewById(R.id.schedule_600_pmText);
            schedule_900_pmView = itemView.findViewById(R.id.schedule_930_pmText);
            onUCLMView = itemView.findViewById(R.id.onUCLMViewText);
            //dateIDView= itemView.findViewById(R.id.dateIDView) ;
        }
    }
}