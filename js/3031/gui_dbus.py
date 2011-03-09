import dbus
import gtk
import dbus.service
from dbus.mainloop.glib import DBusGMainLoop
import pygtk

class gui(dbus.service.Object):
   def on_window_destroy(self, widget, data=None):
   	gtk.main_quit()

   def __init__( self ):
      bus_name = dbus.service.BusName('org.proxy', bus=dbus.SessionBus())
      dbus.service.Object.__init__(self, bus_name, '/org/proxy')

      self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
      self.window.set_size_request(500, 500)
      self.window.set_title("Proxy")

      self.window.connect("delete_event", self.on_window_destroy)
      self.vbox = gtk.VBox(False, 0)
      self.scrolled_window = gtk.ScrolledWindow(hadjustment=None, vadjustment=None)
      self.scrolled_window.show()
     
      self.liststore = gtk.ListStore(str, str, str, 'gboolean')
      self.view = gtk.TreeView(self.liststore)
      self.scrolled_window.add_with_viewport(self.view)
      self.col1 = gtk.TreeViewColumn('Request')
      
      self.view.append_column(self.col1)

      #self.cell = gtk.CellRenderText()
      self.window.show_all()

   @dbus.service.method('org.proxy')
   def append_to_list(self, request):
      print request
      self.traffic.append(request)

def main():
   DBusGMainLoop(set_as_default=True)
   main_gui = gui()
   gtk.main()


if __name__ == '__main__':
   main()
 
