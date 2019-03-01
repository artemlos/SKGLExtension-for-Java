package io.cryptolens;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.cryptolens.internal.BasicResult;
import io.cryptolens.methods.Data;
import io.cryptolens.methods.Helpers;
import io.cryptolens.methods.Key;
import io.cryptolens.models.ActivateModel;
import io.cryptolens.models.DataObject;
import io.cryptolens.models.LicenseKey;
import io.cryptolens.models.ListOfDataObjectsResult;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


/**
 * Unit test for simple App.
 */
public class DataTest
    extends TestCase
{

    HashMap<String, String> APIKey = null;
    LicenseKey license = null;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DataTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DataTest.class );
    }

    public void init() throws Exception{
        String api_content = new String(Files.readAllBytes(Paths.get("apikeys.json")), "UTF-8");
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        APIKey = new Gson().fromJson(api_content, type);

        license = new LicenseKey();
        license.ProductId = 3349;
        license.Key = "GEBNC-WZZJD-VJIHG-GCMVD";
        assertTrue( true );

    }
    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception {


    }

    public void testAdd() throws Exception{

        init();

        BasicResult addResult = Data.AddDataObject(APIKey.get("data"), license, "test", 3, "test");

        if(!Helpers.IsSuccessful(addResult)) {
            fail("Could not add a new data object");
        }

        ListOfDataObjectsResult listResult = Data.ListDataObjects(APIKey.get("data"), license);

        if(!Helpers.IsSuccessful(listResult)) {
            fail("Could not list data objects");
        }

        int count = listResult.DataObjects.size();

        DataObject dObj = listResult.DataObjects.get(0);

        int currentVal = dObj.IntValue;
        BasicResult incrementResult = Data.IncrementIntValue(APIKey.get("data"), license, dObj.Id, 1);
        listResult = Data.ListDataObjects(APIKey.get("data"), license);

        if(!Helpers.IsSuccessful(incrementResult) || listResult.DataObjects.get(0).IntValue != currentVal + 1) {
            fail("Could not increment the data object.");
        }

        BasicResult decrementResult = Data.DecrementIntValue(APIKey.get("data"), license, dObj.Id, 1);
        listResult = Data.ListDataObjects(APIKey.get("data"), license);

        if(!Helpers.IsSuccessful(decrementResult) || listResult.DataObjects.get(0).IntValue != currentVal) {
            fail("Could not increment the data object.");
        }

        BasicResult removeResult = Data.RemoveDataObject(APIKey.get("data"), license, dObj.Id);
        if(!Helpers.IsSuccessful(removeResult)) {
            fail("Could not increment the data object.");
        }

    }
}