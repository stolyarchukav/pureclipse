package #{PACKAGE_NAME}
{	
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;
	
	public class #{CLASS_NAME} extends Proxy
	{
		public static const NAME:String = '#{CLASS_NAME}';
		
		public function #{CLASS_NAME}( ) 
		{
			super ( NAME );
		}

		override public function onRegister():void
		{
			// Add your initialization activities here not in the constructor
            // Add services or delegates and their event listeners, etc.
		}

		override public function onRemove():void
		{
			// Remove any listeners, null references, etc.
		}
	}
}