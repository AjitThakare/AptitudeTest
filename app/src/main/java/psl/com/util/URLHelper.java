package psl.com.util;

import java.util.Map;

/**
 * Created by ajit_thakare on 10/21/2016.
 */
public class URLHelper {
    String url;
    Map<String, String> params;

    public URLHelper( String url,Map<String, String> params) {
        this.params = params;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public String addQueryParam()
    {
        StringBuilder builder = new StringBuilder();
        for(String key: params.keySet())
        {
            Object value = params.get(key);
            if (value != null)
            {
                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);

            }
        }

        url += "?" + builder.toString();
        return url;
    }
}
