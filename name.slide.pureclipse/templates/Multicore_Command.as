package #{PACKAGE_NAME}
{	
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.command.SimpleCommand;

	public class #{CLASS_NAME} extends SimpleCommand
	{
		override public function execute(note:INotification):void
		{
			// Prepare the Model
			//facade.registerProxy( new SomeProxy() );
						
			// Prepare the View
			//var app:Shell = note.getBody() as Shell;
			//facade.registerMediator( new ShellMediator( app ) );
		}
	}
}