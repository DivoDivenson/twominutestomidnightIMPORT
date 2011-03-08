   #!python
import wx

class MyFrame(wx.Frame):
   def __init__(self, parent, ID, title):
       wx.Frame.__init__(self, parent, ID, title, size=(400, 400))
       self.traffic_list = wx.ListCtrl(self, -1, wx.Size(300, 300))


#       panel2.SetBackgroundColour("RED")

       box = wx.FlexGridSizer(wx.VERTICAL)
#       box.Add(panel1, 1, wx.EXPAND)
       #box.Add(panel2, 1, wx.EXPAND)

       self.SetAutoLayout(True)
       self.SetSizer(box)
       self.Layout()


app = wx.PySimpleApp()
frame = MyFrame(None, -1, "Sizer Test")
frame.Show()
app.MainLoop()

