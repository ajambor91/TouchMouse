package aj.phone.client.Activities.SettingsActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import aj.phone.client.IHost;
import aj.phone.client.R;
import aj.phone.client.Utils.Config;

public class HostListAdapter extends RecyclerView.Adapter<HostListAdapter.ViewHolder> {

    private final List<IHost> hostList = new ArrayList<>();
    private int selected = RecyclerView.NO_POSITION;
    private IHost currentHost;

    public HostListAdapter() {
        Config.getInstance().getHosts().forEach((id, host) -> {
            Log.d("Settings", String.format("Host ip: %s", id));
            this.hostList.add(host);
        });
    }

    @NonNull
    @Override
    public HostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Settings", "View holder creating");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostListAdapter.ViewHolder holder, int position) {
        Log.d("Settings", "Bind holder");
        IHost data = this.hostList.get(position);
        Log.d("Settings", String.format("Bind holder with host: %s", data.getHostAddress()));
        holder.textView.setText(data.getHostAddress());
        holder.radioButton.setChecked(position == selected);

        holder.radioButton.setOnClickListener(v -> {

            if (selected != position) {
                selected = position;
                Log.d("Settings", String.format("Selected: %s", position));
                notifyDataSetChanged();
            }
            this.currentHost = this.hostList.get(selected);

        });
    }

    @Override
    public int getItemCount() {
        return hostList.size();

    }

    public IHost getCurrentHost() {
        return this.currentHost;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("Settings", "Creating view holder");
            textView = itemView.findViewById(R.id.hostIp);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
