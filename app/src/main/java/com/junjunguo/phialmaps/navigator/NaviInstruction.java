package com.junjunguo.phialmaps.navigator;

import com.graphhopper.util.Instruction;
import com.junjunguo.phialmaps.map.Navigator;
import com.junjunguo.phialmaps.util.UnitCalculator;
import com.junjunguo.phialmaps.util.Variable;
import com.junjunguo.phialmaps.R;

public class NaviInstruction
{
  String curStreet;
  String nextStreet;
  String nextInstruction;
  String nextInstructionShort;
  String nextInstructionShortFallback;
  long fullTime;
  String fullTimeString;
  double nextDistance;
  int nextSign;
  int nextSignResource;
  
  public NaviInstruction(Instruction in, Instruction nextIn, long fullTime)
  {
    if (nextIn != null)
    {
      nextSign = nextIn.getSign();
      nextSignResource = Navigator.getNavigator().getDirectionSignHuge(nextIn);
      nextStreet = nextIn.getName();
      nextInstruction = Navigator.getNavigator().getDirectionDescription(nextIn, true);
      nextInstructionShort = Navigator.getNavigator().getDirectionDescription(nextIn, false);
      nextInstructionShortFallback = Navigator.getNavigator().getDirectionDescriptionFallback(nextIn, false);
    }
    else
    {
      nextSign = in.getSign(); // Finished?
      nextSignResource = Navigator.getNavigator().getDirectionSignHuge(in);
      nextInstruction = Navigator.getNavigator().getDirectionDescription(in, true);
      nextStreet = in.getName();
      nextInstructionShort = Navigator.getNavigator().getDirectionDescription(in, false);
      nextInstructionShortFallback = Navigator.getNavigator().getDirectionDescriptionFallback(in, false);
    }
    if (nextSignResource == 0) { nextSignResource = R.drawable.ic_2x_continue_on_street; }
    nextDistance = in.getDistance();
    this.fullTime = fullTime;
    fullTimeString = Navigator.getNavigator().getTimeString(fullTime);
    curStreet = in.getName();
    if (curStreet == null) { curStreet = ""; }
    if (nextStreet == null) nextStreet = "";
  }
  
  
  public long getFullTime() { return fullTime; }
  public double getNextDistance() { return nextDistance; }
  public String getFullTimeString() { return fullTimeString; }
  public int getNextSignResource() { return nextSignResource; }
  public int getNextSign() { return nextSign; }
  public String getCurStreet() { return curStreet; }
  public String getNextInstruction() { return nextInstruction; }
  public String getNextStreet() { return nextStreet;}
  public String getNextDistanceString()
  {
    return UnitCalculator.getString(nextDistance);
  }


  public void updateDist(double partDistance)
  {
    nextDistance = partDistance;
  }


  public String getVoiceText()
  {
    String unit = " " + NaviText.sMeters + ". ";
    int roundetDistance = (int)nextDistance;
    if (Variable.getVariable().isImperalUnit())
    {
      unit = " " + NaviText.sFeet + ". ";
      roundetDistance = (int)(nextDistance / UnitCalculator.METERS_OF_FEET);
    }
    roundetDistance = roundetDistance/10;
    roundetDistance = roundetDistance * 10;
    return NaviText.sIn + " " + roundetDistance + unit + nextInstruction;
  }
  
  public String getVoiceTextFallback()
  {
    String unit = " meters. ";
    int roundetDistance = (int)nextDistance;
    if (Variable.getVariable().isImperalUnit())
    {
      unit = " feet. ";
      roundetDistance = (int)(nextDistance / UnitCalculator.METERS_OF_FEET);
    }
    roundetDistance = roundetDistance/10;
    roundetDistance = roundetDistance * 10;
    return "In " + roundetDistance + unit + nextInstructionShortFallback;
  }
}
