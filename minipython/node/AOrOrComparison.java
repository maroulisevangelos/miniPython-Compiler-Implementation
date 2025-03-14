/* This file was generated by SableCC (http://www.sablecc.org/). */

package minipython.node;

import java.util.*;
import minipython.analysis.*;

public final class AOrOrComparison extends POrComparison
{
    private POrComparison _lPar_;
    private POrComparison _rPar_;

    public AOrOrComparison()
    {
    }

    public AOrOrComparison(
        POrComparison _lPar_,
        POrComparison _rPar_)
    {
        setLPar(_lPar_);

        setRPar(_rPar_);

    }
    public Object clone()
    {
        return new AOrOrComparison(
            (POrComparison) cloneNode(_lPar_),
            (POrComparison) cloneNode(_rPar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOrOrComparison(this);
    }

    public POrComparison getLPar()
    {
        return _lPar_;
    }

    public void setLPar(POrComparison node)
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

    public POrComparison getRPar()
    {
        return _rPar_;
    }

    public void setRPar(POrComparison node)
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
            setLPar((POrComparison) newChild);
            return;
        }

        if(_rPar_ == oldChild)
        {
            setRPar((POrComparison) newChild);
            return;
        }

    }
}
