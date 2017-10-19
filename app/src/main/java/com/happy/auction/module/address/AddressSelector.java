package com.happy.auction.module.address;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;
import com.happy.auction.databinding.FragmentAddressSelectorBinding;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by LiuCongshan on 17-10-16.
 * 地址选择器
 */

public class AddressSelector extends BottomSheetDialogFragment {
    private AddressRecord mCurrentProvince;
    private AddressRecord mCurrentCity;
    private AddressRecord mCurrentDistrict;

    private FragmentAddressSelectorBinding mBinding;
    private OnSelectListener mListener;

    public static AddressSelector newInstance(OnSelectListener listener) {
        AddressSelector selector = new AddressSelector();
        selector.mListener = listener;
        return selector;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddressSelectorBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.setFragment(this);

        mBinding.pickProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                onSelectProvince((AddressRecord) data);
            }
        });

        mBinding.pickCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mCurrentCity = (AddressRecord) data;
                mBinding.pickDistrict.setData(mCurrentCity.getChild());
                mBinding.pickDistrict.setSelectedItemPosition(0);
            }
        });

        mBinding.pickDistrict.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mCurrentDistrict = (AddressRecord) data;
            }
        });

        loadData();
    }

    private void onSelectProvince(AddressRecord data) {
        mCurrentProvince = data;
        mBinding.pickCity.setData(mCurrentProvince.getChild());
        mBinding.pickCity.setSelectedItemPosition(0);

        mCurrentCity = mCurrentProvince.getChild().get(0);
        mBinding.pickDistrict.setData(mCurrentCity.getChild());
        mBinding.pickDistrict.setSelectedItemPosition(0);

        mCurrentDistrict = mCurrentCity.getChild().get(0);
    }

    /**
     * 解析省市区的XML数据
     */
    private void loadData() {
        AssetManager asset = getActivity().getAssets();
        InputStream input = null;
        try {
            input = asset.open("province_data.xml");
//           input = getAddressFileInputStream();
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            List<AddressRecord> mProvinceList = handler.getDataList();
            // 初始化默认选中的省、市、区
            if (mProvinceList != null && !mProvinceList.isEmpty()) {
                mBinding.pickProvince.setData(mProvinceList);
                onSelectProvince(mProvinceList.get(0));
            }
        } catch (Throwable ignored) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void onClickCancel(View view) {
        dismissAllowingStateLoss();
    }

    public void onClickSelect(View view) {
        dismissAllowingStateLoss();
        if (mListener != null) {
            mListener.onSelect(mCurrentProvince.name, mCurrentCity.name, mCurrentDistrict.name, mCurrentDistrict.aid);
        }
    }

    public interface OnSelectListener {
        /**
         * 选择完成
         *
         * @param province 省
         * @param city     市
         * @param district 区
         * @param aid      id
         */
        void onSelect(String province, String city, String district, int aid);
    }
}
