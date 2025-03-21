/* This file was generated by SableCC (http://www.sablecc.org/). */

package minipython.node;

import java.util.*;
import minipython.analysis.*;

public final class AModExpression extends PExpression
{
    private PExpression _lPar_;
    private PExpression _rPar_;

    public AModExpression()
    {
    }

    public AModExpression(
        PExpression _lPar_,
        PExpression _rPar_)
    {
        setLPar(_lPar_);

        setRPar(_rPar_);

    }
    public Object clone()
    {
        return new AModExpression(
            (PExpression) cloneNode(_lPar_),
            (PExpression) cloneNode(_rPar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAModExpression(this);
    }

    public PExpression getLPar()
    {
        return _lPar_;
    }

    public void setLPar(PExpression node)
    {
        if(_lPar_ != null)
        {
            _lPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lPar_ = node;
    }

    public PExpression getRPar()
    {
        return _rPar_;
    }

    public void setRPar(PExpression node)
    {
        if(_rPar_ != null)
        {
            _rPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rPar_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_lPar_)
            + toString(_rPar_);
    }

    void removeChild(Node child)
    {
        if(_lPar_ == child)
        {
            _lPar_ = null;
            return;
        }

        if(_rPar_ == child)
        {
            _rPar_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_lPar_ == oldChild)
        {
            setLPar((PExpression) newChild);
            return;
        }

        if(_rPar_ == oldChild)
        {
            setRPar((PExpression) newChild);
            return;
        }

    }
}
