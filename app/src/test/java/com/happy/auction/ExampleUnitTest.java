package com.happy.auction;

import com.happy.auction.entity.AuctionDetailParam;
import com.happy.auction.entity.BaseRequest;
import com.happy.auction.utils.GsonSingleton;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        BaseRequest<AuctionDetailParam> param = new BaseRequest<>();
        param.action = "bid";
        AuctionDetailParam data = new AuctionDetailParam();
        data.uid = "10086";
        data.sid = "10086";
        param.data = data;
        String message = GsonSingleton.get().toJson(param);
        System.out.println("message : " + message);

        assertEquals(4, 2 + 2);
    }
}