// Copyright 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry5.portlet.upload.internal.services;

import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newMap;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.ActionRequest;

import org.apache.tapestry5.upload.internal.services.ParameterValue;

/**
 * @author Raphael Jean
 */
public class ParametersPortletRequestWrapper extends ActionRequestWrapper
{

    /**
     * Map of {@link ValuePart}&nbsp;keyed on parameter name.
     */
	private final Map<String, ParameterValue> _parameters = newMap();

    public ParametersPortletRequestWrapper(ActionRequest request)
    {
        super(request);
    }

    public String getParameter(String name)
    {
        String[] values = getParameterValues(name);

        return (values == null || values.length == 0) ? super.getParameter(name) : values[0];
    }

    public Map getParameterMap()
    {
        Map<String, Object> paramMap = newMap();

        for (Map.Entry<String, ParameterValue> e : _parameters.entrySet())
        {
            ParameterValue value = e.getValue();

            paramMap.put(e.getKey(), value.isMulti() ? value.multi() : value.single());
        }

        return paramMap;
    }

    @SuppressWarnings("unchecked")
    public Enumeration getParameterNames()
    {
    	return Collections.enumeration(_parameters.keySet());
    }

    public String[] getParameterValues(String name)
    {
        return getValueFor(name).multi();
    }
    
    public void addParameter(String name, String value)
    {
        ParameterValue pv = _parameters.get(name);
        if (pv == null)
        {
            pv = new ParameterValue(value);
            _parameters.put(name, pv);
        }
        else
        {
            pv.add(value);
        }
    }
    
    ParameterValue getValueFor(String name)
    {
        ParameterValue value = _parameters.get(name);

        return value == null ? ParameterValue.NULL : value;
    }

    public String toString()
    {
        return "<UploadFormPortletParametersWrapper for " + getRequest() + ">";
    }

}
