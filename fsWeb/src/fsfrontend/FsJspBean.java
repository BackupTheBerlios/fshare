package fsfrontend;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FsJspBean
{
  private String sample = "Start value";

  //Access sample property
  public String getSample()
  {
    return sample;
  }

  //Access sample property
  public void setSample(String newValue)
  {
    if (newValue!=null)
    {
      sample = newValue;
    }
  }
}
