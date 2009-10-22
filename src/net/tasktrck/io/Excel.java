package net.tasktrck.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.tasktrck.model.TaskEntry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.jdesktop.swingx.JXTable;

public class Excel
{
	public static boolean export(File file, int[] selectedRows, JXTable taskTable)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Project 1");
		HSSFDataFormat df = wb.createDataFormat();

		HSSFFont headFont = wb.createFont();
		headFont.setFontHeightInPoints((short) 16);
		headFont.setFontName("Segoe UI");
		HSSFCellStyle headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);

		HSSFFont colFont = wb.createFont();
		colFont.setFontHeightInPoints((short) 11);
		colFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		colFont.setFontName("Calibri");
		HSSFCellStyle colStyle = wb.createCellStyle();
		colStyle.setFont(colFont);

		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("TaskTrckr Export"));
		cell.setCellStyle(headStyle);

		HSSFCellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(df.getFormat("m-d-yy h:mm:ss"));

		HSSFCellStyle timeStyle = wb.createCellStyle();
		timeStyle.setDataFormat(df.getFormat("h:mm:ss"));

		sheet.addMergedRegion(new CellRangeAddress(0, //first row (0-based)
				0, //last row  (0-based)
				0, //first column (0-based)
				taskTable.getColumnCount() - 1 //last column  (0-based)
				));

		int duration = -1, start = -1, end = -1;

		//Print the column headers
		row = sheet.createRow(2);
		for (int h = 0; h < taskTable.getColumnCount(); h++)
		{
			cell = row.createCell(h);
			cell.setCellStyle(colStyle);
			cell.setCellValue(new HSSFRichTextString(taskTable.getColumnName(h)));

			if (taskTable.getColumnName(h).equals("Duration"))
				duration = h;
			else if (taskTable.getColumnName(h).equals("Start"))
				start = h;
			else if (taskTable.getColumnName(h).equals("End"))
				end = h;
		}

		//Loop through the selected rows and add them to the file.
		int i = 3;
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
		for (int r : selectedRows)
		{
			row = sheet.createRow(i);
			for (int j = 0; j < taskTable.getColumnCount(); j++)
			{
				cell = row.createCell(j);

				//Set the right datastyling.
				if (j == end || j == start)
				{
					cell.setCellStyle(dateStyle);
					cell.setCellValue((Date) taskTable.getValueAt(r, j));
				} else if (j == duration)
				{
					cell.setCellStyle(timeStyle);
					cell.setCellValue(new Date(((TaskEntry) taskTable.getValueAt(r, j)).getDuration()));
				} else
					cell.setCellValue(new HSSFRichTextString(taskTable.getValueAt(r, j).toString()));
			}
			i++;
		}

		//Set the duration sum.
		row = sheet.createRow(i);
		char colN = getExcelChar(duration);
		cell = row.createCell(duration);
		cell.setCellStyle(timeStyle);
		cell.setCellFormula("SUM(" + colN + "4:" + colN + "" + i + ")");

		//Adjust the column widths to fit the contents;
		for (short k = 0; k < 5; k++)
			sheet.autoSizeColumn(k);

		return saveFile(file, wb);
	}

	private static boolean saveFile(File file, HSSFWorkbook wb)
	{
		boolean output = true;
		// Write the output to a file
		FileOutputStream fileOut;
		try
		{
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "An error occurred with the following message:\n\n" + e.getMessage(), "An error occurred",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			output = false;
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "An error occurred with the following message:\n\n" + e.getMessage(), "An error occurred",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			output = false;
		}
		return output;
	}

	private static char getExcelChar(int i)
	{
		return (char) ('A' + i);
	}
}