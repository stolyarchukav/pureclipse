package #{PACKAGE_NAME}
{	
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.utilities.fabrication.patterns.mediator.FlexMediator;
	
	import #{SELECTED_VIEW_CLASS_PATH};

	public class #{CLASS_NAME} extends FlexMediator
	{
		public static const NAME:String = '#{CLASS_NAME}';

		public function #{CLASS_NAME}( viewComponent:Object ) 
		{
			super(NAME, viewComponent);   
		}
		
		override public function onRegister():void 
		{			
			super.onRegister();
			
			// add listeners
		}
		
		override public function onRemove():void 
		{			
			super.onRemove();
			
			// remove listeners
		}
		
		public function get component():#{SELECTED_VIEW_CLASS} 
		{
			return viewComponent as #{SELECTED_VIEW_CLASS};
		}
	}
}