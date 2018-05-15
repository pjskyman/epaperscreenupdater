package sky.epaperscreenupdater;

public enum RefreshType
{
    PARTIAL_REFRESH
    {
        public RefreshType combine(RefreshType refreshType)
        {
            if(refreshType==PARTIAL_REFRESH)
                return PARTIAL_REFRESH;
            else
                if(refreshType==TOTAL_REFRESH)
                    return TOTAL_REFRESH;
                else
                    if(refreshType==PARTIAL_REFRESH_IN_FAST_MODE)
                        return PARTIAL_REFRESH_IN_FAST_MODE;
            return PARTIAL_REFRESH;
        }
    },
    TOTAL_REFRESH
    {
        public RefreshType combine(RefreshType refreshType)
        {
            if(refreshType==PARTIAL_REFRESH)
                return TOTAL_REFRESH;
            else
                if(refreshType==TOTAL_REFRESH)
                    return TOTAL_REFRESH;
                else
                    if(refreshType==PARTIAL_REFRESH_IN_FAST_MODE)
                        return TOTAL_REFRESH;
            return TOTAL_REFRESH;
        }
    },
    PARTIAL_REFRESH_IN_FAST_MODE
    {
        public RefreshType combine(RefreshType refreshType)
        {
            if(refreshType==PARTIAL_REFRESH)
                return PARTIAL_REFRESH_IN_FAST_MODE;
            else
                if(refreshType==TOTAL_REFRESH)
                    return TOTAL_REFRESH;
                else
                    if(refreshType==PARTIAL_REFRESH_IN_FAST_MODE)
                        return PARTIAL_REFRESH_IN_FAST_MODE;
            return PARTIAL_REFRESH_IN_FAST_MODE;
        }
    },
    ;

    public abstract RefreshType combine(RefreshType refreshType);

    public boolean isPartialRefresh()
    {
        return !isTotalRefresh();
    }

    public boolean isTotalRefresh()
    {
        return this==TOTAL_REFRESH;
    }

    public boolean isFastMode()
    {
        return this==PARTIAL_REFRESH_IN_FAST_MODE;
    }
}
