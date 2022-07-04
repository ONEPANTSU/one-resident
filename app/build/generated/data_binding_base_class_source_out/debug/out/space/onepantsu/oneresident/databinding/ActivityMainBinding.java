// Generated by view binder compiler. Do not edit!
package space.onepantsu.oneresident.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import space.onepantsu.oneresident.R;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button historyButton;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final Button paymentButton;

  @NonNull
  public final Button residentButton;

  @NonNull
  public final Button settingsButton;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button historyButton,
      @NonNull ImageView imageView, @NonNull Button paymentButton, @NonNull Button residentButton,
      @NonNull Button settingsButton) {
    this.rootView = rootView;
    this.historyButton = historyButton;
    this.imageView = imageView;
    this.paymentButton = paymentButton;
    this.residentButton = residentButton;
    this.settingsButton = settingsButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.historyButton;
      Button historyButton = ViewBindings.findChildViewById(rootView, id);
      if (historyButton == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.paymentButton;
      Button paymentButton = ViewBindings.findChildViewById(rootView, id);
      if (paymentButton == null) {
        break missingId;
      }

      id = R.id.residentButton;
      Button residentButton = ViewBindings.findChildViewById(rootView, id);
      if (residentButton == null) {
        break missingId;
      }

      id = R.id.settingsButton;
      Button settingsButton = ViewBindings.findChildViewById(rootView, id);
      if (settingsButton == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, historyButton, imageView,
          paymentButton, residentButton, settingsButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}