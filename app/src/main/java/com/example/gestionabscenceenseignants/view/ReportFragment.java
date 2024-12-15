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

    // Méthode appelée pour créer et retourner la vue du fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charge le layout du fragment, qui est défini dans fragment_report.xml
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        // Trouve la WebView dans la vue du fragment à partir du layout
        WebView webView = rootView.findViewById(R.id.webView);

        // Active et configure les paramètres de la WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Active le JavaScript dans la WebView pour les contenus interactifs
        webSettings.setLoadWithOverviewMode(true); // Ajuste le contenu pour qu'il soit visible à l'échelle de l'écran
        webSettings.setUseWideViewPort(true); // Utilise un large viewport pour garantir une bonne présentation sur les appareils mobiles

        // URL du rapport Power BI à charger dans la WebView avec l'option mobile activée
        String powerBiUrl = "https://app.powerbi.com/view?r=eyJrIjoiNmIzMTA3NzgtMWE0Ny00ZjJmLTkwZDktZDhmN2JiNTdkNDViIiwidCI6ImRiZDY2NjRkLTRlYjktNDZlYi05OWQ4LTVjNDNiYTE1M2M2MSIsImMiOjl9&pageName=ReportSection&mobile=true";

        // Charge l'URL dans la WebView
        webView.loadUrl(powerBiUrl);

        // Retourne la vue du fragment
        return rootView;
    }
}
