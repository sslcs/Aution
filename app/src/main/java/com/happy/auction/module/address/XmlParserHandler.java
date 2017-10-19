package com.happy.auction.module.address;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimny
 * on 15-1-10.
 */
public class XmlParserHandler extends DefaultHandler {
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";
    private static final String KEY_DISTRICT = "district";

    private AddressRecord mProvince = new AddressRecord();
    private AddressRecord mCity = new AddressRecord();
    private AddressRecord mDistrict = new AddressRecord();

    /**
     * 存储所有的解析对象
     */
    private List<AddressRecord> mProvinceList = new ArrayList<>();

    public XmlParserHandler() {
    }

    public List<AddressRecord> getDataList() {
        return mProvinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (KEY_PROVINCE.equals(qName)) {
            mProvince = new AddressRecord();
            mProvince.name = attributes.getValue(0);
            mProvince.child = new ArrayList<>();
        } else if (KEY_CITY.equals(qName)) {
            mCity = new AddressRecord();
            mCity.name = attributes.getValue(0);
            mCity.child = new ArrayList<>();
        } else if (KEY_DISTRICT.equals(qName)) {
            mDistrict = new AddressRecord();
            mDistrict.name = attributes.getValue(0);
            mDistrict.aid = Integer.parseInt(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (KEY_DISTRICT.equals(qName)) {
            mCity.child.add(mDistrict);
        } else if (KEY_CITY.equals(qName)) {
            mProvince.child.add(mCity);
        } else if (KEY_PROVINCE.equals(qName)) {
            mProvinceList.add(mProvince);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}
