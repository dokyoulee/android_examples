package exam.sai.com.designpattern;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.sai.com.designpattern.databinding.FragmentDesignPatternBinding;

public class EmpFragment extends Fragment {
    private FragmentDesignPatternBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_design_pattern, container, false);
        mBinding = FragmentDesignPatternBinding.bind(v);
        mBinding.setVariable(BR.fragment, this);
        return v;
    }

    public void onButtonClick(View v) {

    }
}
