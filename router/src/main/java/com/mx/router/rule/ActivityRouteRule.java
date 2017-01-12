package com.mx.router.rule;

import android.app.Activity;
import android.content.Intent;

import com.mx.activitystarter.ActivityResultCallback;
import com.mx.engine.utils.CheckUtils;
import com.mx.router.Pipe;
import com.mx.router.RouteRule;

import java.util.List;

/**
 * Created by chenbaocheng on 16/12/10.
 */

public class ActivityRouteRule implements RouteRule {
    private final Class<? extends Activity> activityClass;

    public ActivityRouteRule(Class<? extends Activity> activityClass) {
        CheckUtils.checkNotNull(activityClass);
        this.activityClass = activityClass;
    }

    @Override
    public void handleRoute(final Pipe pipe) {
        if (pipe == null) {
            return;
        }

        Intent intent = new Intent(pipe.getContext(), activityClass);
        for (String name : pipe.getUri().getQueryParameterNames()) {
            List<String> values = pipe.getParameters(name);
            if (values.size() == 1) {
                intent.putExtra(name, values.get(0));
            } else if (values.size() > 1) {
                String[] strValues = new String[values.size()];
                values.toArray(strValues);
                intent.putExtra(name, strValues);
            }
        }
        try {
            pipe.getActivityStarter().startActivityForResult(intent, new ActivityResultCallback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    ActivityRouteRule.this.onActivityResult(pipe, resultCode, data);
                }
            });
        } catch (Throwable t) {
            ActivityRouteRule.this.handleStartActivityException(pipe, t);
        }
    }

    protected void onActivityResult(Pipe pipe, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            pipe.success(data == null ? null : data.getExtras());
        } else if (resultCode == Activity.RESULT_CANCELED) {
            pipe.cancel();
        } else {
            pipe.fail(resultCode, String.format("Activity %s is failed due to unknown reason", activityClass.getSimpleName()), null);
        }
    }

    protected void handleStartActivityException(Pipe pipe, Throwable t) {
        pipe.fail(String.format("Error in starting Activity %s.", activityClass.getSimpleName()), t);
    }
}
