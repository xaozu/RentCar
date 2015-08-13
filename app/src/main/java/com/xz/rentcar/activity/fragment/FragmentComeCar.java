package com.xz.rentcar.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xz.rentcar.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 送车到家
 */
public class FragmentComeCar extends Fragment {

    @InjectView(R.id.edit_give_time)
    MaterialEditText editGiveTime;
    @InjectView(R.id.edit_address)
    MaterialEditText editAddress;
    @InjectView(R.id.edit_time)
    MaterialEditText editTime;
    @InjectView(R.id.edit_user)
    MaterialEditText editUser;
    @InjectView(R.id.btn_give)
    Button btnGive;
    private View view;

    public void setmReserverListener(ReserveListener mReserverListener) {
        this.mReserverListener = mReserverListener;
    }

    private ReserveListener mReserverListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_come_car, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReserverListener.reserveClick();
//                MainActivity main= (MainActivity) getActivity();
//                main.preferenceUtils.saveParam(main.CAR_STATE_KEY, main.CAR_STATE_STAR);
//                main.initState();
//                SnackbarUtils.show(getActivity(),R.string.reserve_success);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public interface ReserveListener{
        public void reserveClick();
    }
}
