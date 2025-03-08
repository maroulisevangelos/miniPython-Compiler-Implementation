/* This file was generated by SableCC (http://www.sablecc.org/). */

package minipython.node;

import minipython.analysis.*;

public final class TLogicPlus extends Token
{
    public TLogicPlus()
    {
        super.setText("&&");
    }

    public TLogicPlus(int line, int pos)
    {
        super.setText("&&");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLogicPlus(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLogicPlus(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLogicPlus text.");
    }
}
