package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.gestionabscenceenseignants.R;

public class ReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charger le layout du fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        // Trouve la WebView dans le layout
        WebView webView = rootView.findViewById(R.id.webView);

        // Active JavaScript et optimise les paramètres WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Active JavaScript
        webSettings.setLoadWithOverviewMode(true); // Charge le contenu pour qu'il s'adapte à la taille de l'écran
        webSettings.setUseWideViewPort(true); // Utilise un viewport large pour respecter les proportions mobiles
        // Charge l'URL Power BI avec l'option `mobile=true`
        String powerBiUrl = "https://app.powerbi.com/view?r=eyJrIjoiNmIzMTA3NzgtMWE0Ny00ZjJmLTkwZDktZDhmN2JiNTdkNDViIiwidCI6ImRiZDY2NjRkLTRlYjktNDZlYi05OWQ4LTVjNDNiYTE1M2M2MSIsImMiOjl9&pageName=ReportSection&mobile=true";
        webView.loadUrl(powerBiUrl);

        return rootView; // Retourne la vue pour ce fragment
    }
}
