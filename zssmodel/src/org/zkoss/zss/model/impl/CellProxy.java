/*

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		2013/12/01 , Created by dennis
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zss.model.impl;

import org.zkoss.poi.ss.formula.eval.ValueEval;
import org.zkoss.zss.model.*;
import org.zkoss.zss.model.sys.dependency.Ref;
import org.zkoss.zss.model.sys.formula.FormulaExpression;

import java.sql.Connection;
import java.util.Locale;
/**
 * 
 * @author dennis
 * @since 3.5.0
 */
class CellProxy extends AbstractCellAdv {
	private static final long serialVersionUID = 1L;
	AbstractCellAdv _proxy;
	private int _rowIdx;
	private int _columnIdx;
    private AbstractSheetAdv _sheet;

	public CellProxy(AbstractSheetAdv sheet, int row, int column) {
        this._sheet = sheet;
        this._rowIdx = row;
		this._columnIdx = column;
	}

	@Override
	public SSheet getSheet() {
		if(_proxy!=null){
			return _proxy.getSheet();
		}
        return _sheet;
    }

    @Override
    public void setSheet(AbstractSheetAdv sheet) {
		_sheet = sheet;
    }

	private void loadProxy() {
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getCell(_rowIdx, _columnIdx, false);
		}
	}

	@Override
	public boolean isNull() {
		loadProxy();
		//if any data in data grid and it is not null, you should handle it.
		if(_proxy==null){
			return true;
		}else{
			return _proxy.isNull();
		}
	}

	@Override
	public CellType getType() {
		loadProxy();
		if(_proxy==null){
			return CellType.BLANK;
		}else{
			return  _proxy.getType();
		}
	}

	@Override
	public int getRowIndex() {
		loadProxy();
		return _proxy == null ? _rowIdx : _proxy.getRowIndex();
	}

	@Override
	public void setRow(int row) {
        throw new UnsupportedOperationException("readonly");
	}

	@Override
	public int getColumnIndex() {
		loadProxy();
		return _proxy == null ? _columnIdx : _proxy.getColumnIndex();
	}

	@Override
	public void setColumn(int column) {
        throw new UnsupportedOperationException("readonly");
	}

	@Override
	public void setFormulaValue(String formula, Connection connection, boolean updateToDB) {
		loadProxy();
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
			_proxy.setFormulaValue(formula, connection, updateToDB);
		} else if (_proxy != null) {
			_proxy.setFormulaValue(formula,connection, updateToDB);
		}
	}

	//ZSS-565: Support input with Swedish locale into Formula
	@Override
	public void setFormulaValue(String formula, Locale locale, Connection connection, boolean updateToDB) {
		loadProxy();
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
			_proxy.setFormulaValue(formula, locale, connection, updateToDB);
		} else if (_proxy != null) {
			_proxy.setFormulaValue(formula, locale, connection, updateToDB);
		}
	}

	@Override
	public void setValue(Object value, Connection connection, boolean updateToDB) {
		loadProxy();
		if (_proxy == null && value != null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
			_proxy.setValue(value, connection, updateToDB);
		} else if (_proxy != null) {
			_proxy.setValue(value, connection, updateToDB);
		}
	}

	//ZSS-853
	@Override
	protected void setValue(Object value, boolean aString, Connection connection, boolean updateToDB) {
		//loadProxy();
		if (_proxy == null && value != null) {
            // Create a new cell object
            _proxy = new CellImpl();
            _proxy.setRow(_rowIdx);
            _proxy.setColumn(_columnIdx);
            _proxy.setSheet(_sheet);
            _proxy.setValue(value, aString, connection, updateToDB);
		} else if (_proxy != null) {
			_proxy.setValue(value, aString, connection, updateToDB);
		}
	}

	@Override
	public Object getValue() {
		loadProxy();
		if(_proxy==null){
			return null;
		}else{
			return  _proxy.getValue();
		}
	}

	@Override
	public String getReferenceString() {
		loadProxy();
		return _proxy == null ? new CellRegion(_rowIdx, _columnIdx).getReferenceString() : _proxy.getReferenceString();
	}

	@Override
	public SCellStyle getCellStyle() {
		return getCellStyle(false);
	}

	@Override
	public void setCellStyle(SCellStyle cellStyle) {
		loadProxy();
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
		}
		_proxy.setCellStyle(cellStyle);
	}

	@Override
	public SCellStyle getCellStyle(boolean local) {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getCellStyle(local);
		}
		if (local)
			return null;
		AbstractSheetAdv sheet =  ((AbstractSheetAdv)getSheet());
		AbstractRowAdv row = sheet.getRow(_rowIdx, false);
		SCellStyle style = null;
		if (row != null) {
			style = row.getCellStyle(true);
		}
		if (style == null) {
			AbstractColumnArrayAdv carr = (AbstractColumnArrayAdv)sheet.getColumnArray(_columnIdx);
			if (carr != null) {
				style = carr.getCellStyle(true);
			}
		}
		if (style == null) {
			style = sheet.getBook().getDefaultCellStyle();
		}
		return style;
	}

	@Override
	public CellType getFormulaResultType() {
		loadProxy();
		return _proxy == null ? null : _proxy.getFormulaResultType();
	}

	@Override
	public void clearValue(Connection connection, boolean updateToDB) {
		loadProxy();
		if (_proxy != null)
			_proxy.clearValue(connection, updateToDB);
	}

	@Override
	public void clearFormulaResultCache() {
		loadProxy();
		if (_proxy != null)
			_proxy.clearFormulaResultCache();
	}

	@Override
	protected void evalFormula() {
		loadProxy();
		if (_proxy != null)
			_proxy.evalFormula();
	}

	@Override
	protected Object getValue(boolean eval) {
		loadProxy();
		return _proxy == null ? null : _proxy.getValue(eval);
	}

	@Override
	public SHyperlink getHyperlink() {
		loadProxy();
		return _proxy == null ? null : _proxy.getHyperlink();
	}

	@Override
	public void setHyperlink(SHyperlink hyperlink) {
		loadProxy();
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
		}
		_proxy.setHyperlink(hyperlink);
	}

	@Override
	public SComment getComment() {
		loadProxy();
		return _proxy == null ? null : _proxy.getComment();
	}

	@Override
	public void setComment(SComment comment) {
		loadProxy();
		if (_proxy == null) {
			_proxy = ((AbstractSheetAdv) getSheet()).getOrCreateRow(
					_rowIdx).getOrCreateCell(_columnIdx);
		}
		_proxy.setComment(comment);
	}

	@Override
	public boolean isFormulaParsingError() {
		loadProxy();
		return _proxy != null && _proxy.isFormulaParsingError();
	}

	protected Ref getRef(){
		return new RefImpl(this);
	}

	//ZSS-688
	//@since 3.6.0
    /* TODO: Remove the idea of clone cell. For sheet cloning use data modle cloning */
    /*
	@Override
    AbstractCellAdv cloneCell(AbstractRowAdv row, Connection connection, boolean updateToDB) {
		if (_proxy == null) {
			return new CellProxy((AbstractSheetAdv)row.getSheet(), row.getIndex(), this.getColumnIndex());
		} else {
			return _proxy.cloneCell(row, connection, updateToDB);
		}
	} */

	@Override
	public void setFormulaResultValue(ValueEval value) {
		loadProxy();
		if (_proxy != null) {
			_proxy.setFormulaResultValue(value);
		}
	}

	@Override
	public void deleteComment() {
		loadProxy();
		if (_proxy != null) {
			_proxy.deleteComment();
		}
	}

	@Override
	public FormulaExpression getFormulaExpression() {
		loadProxy();
		if (_proxy != null) {
			return _proxy.getFormulaExpression();
		}
		return null;
	}

	@Override
	protected byte[] toBytes() {
		return new byte[0];
	}
}
