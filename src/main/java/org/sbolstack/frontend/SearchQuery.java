
package org.sbolstack.frontend;

import java.util.ArrayList;

public class SearchQuery
{
    public Integer offset;
    public Integer limit;

    public ArrayList<SearchCriteria> criteria
            = new ArrayList<SearchCriteria>();
}
