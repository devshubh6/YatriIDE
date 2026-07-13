package com.shubh.yatri.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.shubh.yatri.R;
import com.shubh.yatri.api.ApiClient;
import com.shubh.yatri.api.ApiService;
import com.shubh.yatri.utils.ToastHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class CodeAdapter extends ArrayAdapter<JsonObject> {

    private Context context;
    private List<JsonObject> codes;
    private ApiService apiService;

    public CodeAdapter(Context context, List<JsonObject> codes) {
        super(context, 0, codes);
        this.context = context;
        this.codes = codes;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_code, parent, false);
        }

        JsonObject code = codes.get(position);

        TextView titleView = convertView.findViewById(R.id.codeTitle);
        TextView languageView = convertView.findViewById(R.id.codeLanguage);
        TextView statusView = convertView.findViewById(R.id.compilationStatus);
        Button deleteBtn = convertView.findViewById(R.id.deleteBtn);

        String title = code.has("title") ? code.get("title").getAsString() : "Untitled";
        String language = code.has("language") ? code.get("language").getAsString() : "java";
        String status = code.has("compilationStatus") ? code.get("compilationStatus").getAsString() : "none";

        titleView.setText(title);
        languageView.setText("Language: " + language);
        statusView.setText("Status: " + status);

        deleteBtn.setOnClickListener(v -> {
            String codeId = code.get("_id").getAsString();
            deleteCode(codeId, position);
        });

        return convertView;
    }

    private void deleteCode(String codeId, int position) {
        apiService.deleteCode(codeId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    codes.remove(position);
                    notifyDataSetChanged();
                    ToastHelper.showShort(context, "✅ Code deleted!");
                } else {
                    ToastHelper.showShort(context, "❌ Failed to delete");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ToastHelper.showShort(context, "❌ Error: " + t.getMessage());
            }
        });
    }
}
