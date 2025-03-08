/* This file was generated by SableCC (http://www.sablecc.org/). */

package minipython.node;

import java.util.*;
import minipython.analysis.*;

public final class APrintStatement extends PStatement
{
    private PExpression _expression_;
    private final LinkedList _comExp_ = new TypedLinkedList(new ComExp_Cast());

    public APrintStatement()
    {
    }

    public APrintStatement(
        PExpression _expression_,
        List _comExp_)
    {
        setExpression(_expression_);

        {
            this._comExp_.clear();
            this._comExp_.addAll(_comExp_);
        }

    }
    public Object clone()
    {
        return new APrintStatement(
            (PExpression) cloneNode(_expression_),
            cloneList(_comExp_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPrintStatement(this);
    }

    public PExpression getExpression()
    {
        return _expression_;
    }

    public void setExpression(PExpression node)
    {
        if(_expression_ != null)
        {
            _expression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _expression_ = node;
    }

    public LinkedList getComExp()
    {
        return _comExp_;
    }

    public void setComExp(List list)
    {
        _comExp_.clear();
        _comExp_.addAll(list);
    }

    public String toString()
    {
        return ""
            + toString(_expression_)
            + toString(_comExp_);
    }

    void removeChild(Node child)
    {
        if(_expression_ == child)
        {
            _expression_ = null;
            return;
        }

        if(_comExp_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_expression_ == oldChild)
        {
            setExpression((PExpression) newChild);
            return;
        }

        for(ListIterator i = _comExp_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class ComExp_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PComExp node = (PComExp) o;

            if((node.parent() != null) &&
                (node.parent() != APrintStatement.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != APrintStatement.this))
            {
                node.parent(APrintStatement.this);
            }

            return node;
        }
    }
}
