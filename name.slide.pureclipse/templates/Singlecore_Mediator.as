package #{PACKAGE_NAME}
{		
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	import #{SELECTED_VIEW_CLASS_PATH};
	
	public class #{CLASS_NAME} extends Mediator
	{
		public static const NAME:String = '#{CLASS_NAME}';
		
 		public function #{CLASS_NAME}( viewComponent:Object ) 
		{
			super( NAME, viewComponent );
		}
		
		override public function onRegister():void 
		{			
			super.onRegister();
			
			// Add listeners
		}
		
		override public function onRemove():void 
		{			
			super.onRemove();
			
			// Remove listeners
		}

		override public function listNotificationInterests():Array 
		{
			
			return [ //ApplicationConstants.SOME_NOTE
			       ];
		}

		override public function handleNotification( note:INotification ):void 
		{
			/*
			switch ( note.getName() ) 
			{
				case ApplicationConstants.SOME_NOTE:
					myComponent.someProp = note.getBody() as SomeClass;
					break;
			}
			*/
		}		

		public function get component():#{SELECTED_VIEW_CLASS} 
		{
			return viewComponent as #{SELECTED_VIEW_CLASS};
		}
	}
}