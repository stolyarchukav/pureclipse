package #{PACKAGE_NAME}
{	
	import org.puremvc.as3.multicore.utilities.fabrication.patterns.proxy.FabricationProxy;
	
	public class #{CLASS_NAME} extends FabricationProxy
	{
		public static const NAME : String = '#{CLASS_NAME}';
		
		/**
		 * Constructor. 
		 */
		public function #{CLASS_NAME}(data:Object=null)
		{
			super(NAME, data);			
		}
	}
}