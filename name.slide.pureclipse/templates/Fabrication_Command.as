package #{PACKAGE_NAME}
{
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.utilities.fabrication.patterns.command.SimpleFabricationCommand;
	
	public class #{CLASS_NAME} extends SimpleFabricationCommand
	{
		override public function execute(note:INotification):void
		{
			// put your command logic here
		}
	}
}