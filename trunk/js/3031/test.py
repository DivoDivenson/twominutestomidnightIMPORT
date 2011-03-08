#!/usr/bin/env python
# -*- coding: utf-8 -*-
# generated by wxGlade 0.6.3 on Mon Mar  7 22:55:30 2011

import wx

# begin wxGlade: extracode
# end wxGlade



class ProxyGUI(wx.Frame):
    def __init__(self, *args, **kwds):
        # begin wxGlade: ProxyGUI.__init__
        kwds["style"] = wx.DEFAULT_FRAME_STYLE
        wx.Frame.__init__(self, *args, **kwds)
        self.list_ctrl_1 = wx.ListCtrl(self, -1, style=wx.LC_REPORT|wx.SUNKEN_BORDER)
        self.panel_2 = wx.Panel(self, -1)
        self.panel_1 = wx.Panel(self, -1)
        self.button_1 = wx.Button(self, -1, "button_1")

        self.__set_properties()
        self.__do_layout()
	self.init_list()
        # end wxGlade

    def __set_properties(self):
        # begin wxGlade: ProxyGUI.__set_properties
        self.SetTitle("frame_1")
        self.SetSize((500, 500))
        # end wxGlade

    def __do_layout(self):
        # begin wxGlade: ProxyGUI.__do_layout
        grid_sizer_1 = wx.FlexGridSizer(3, 3, 0, 0)
        grid_sizer_1.Add(self.list_ctrl_1, 1, wx.EXPAND, 0)
        grid_sizer_1.Add(self.panel_2, 1, wx.EXPAND, 0)
        grid_sizer_1.Add(self.panel_1, 1, wx.EXPAND, 0)
        grid_sizer_1.Add(self.button_1, 0, 0, 0)
        self.SetSizer(grid_sizer_1)
        grid_sizer_1.AddGrowableRow(0)
        grid_sizer_1.AddGrowableRow(1)
        grid_sizer_1.AddGrowableRow(2)
        grid_sizer_1.AddGrowableCol(0)
        grid_sizer_1.AddGrowableCol(1)
        grid_sizer_1.AddGrowableCol(2)
        self.Layout()
        self.SetSize((500, 500))
        # end wxGlade

    def init_list(self):
    	self.col0 = wx.ListItem()
	self.col0.SetId(0)
	self.col0.SetText("Address")
	self.col0.SetWidth(100)
	self.list_ctrl_1.InsertColumn(0, "col0")
	self.list_ctrl_1.InsertStringItem(0, "Test")
	self.list_ctrl_1.InsertStringItem(0, "Test2")

# end of class ProxyGUI


if __name__ == "__main__":
    app = wx.PySimpleApp(0)
    wx.InitAllImageHandlers()
    frame_1 = ProxyGUI(None, -1, "")
    app.SetTopWindow(frame_1)
    frame_1.Show()
    app.MainLoop()
