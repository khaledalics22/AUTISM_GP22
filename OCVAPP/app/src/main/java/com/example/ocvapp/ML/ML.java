package com.example.ocvapp.ML;

import android.content.Context;

import com.example.Interfaces.ER;
import com.example.Interfaces.FD;
import com.example.ocvapp.CustomER.CustomER;
import com.example.ocvapp.CustomFD.CustomFD;
import com.example.ocvapp.ReadyFD.ReadyER;
import com.example.ocvapp.ReadyFD.ReadyFD;

public class ML {
    private static ModelType type;
    private static FD instanceFD = null;
    private static ER instanceER = null;

    public static ER getERModel() {
        if (type == null || instanceER == null) {
            return null;
        }
        return instanceER;

    }

    public static FD getFDModel() {
        if (type == null || instanceFD == null || instanceER == null) {
            return null;
        }
        return instanceFD;

    }

    public static void buildFDModel(Context context, ModelType new_type) {
        type = new_type;
        switch (type) {
            case CUSTOM:
                instanceFD = CustomFD.getInstance();
                instanceER = CustomER.getInstance();
                break;
            case READY:
                instanceER = ReadyER.getInstance();

//                instanceFD = CustomFD.getInstance();
                instanceFD = ReadyFD.getInstance();
                break;
        }
    }

    public enum ModelType {
        CUSTOM,
        READY,
    }
}
