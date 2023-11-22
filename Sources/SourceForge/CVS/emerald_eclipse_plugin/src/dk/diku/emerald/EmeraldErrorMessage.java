/*
 * Created on Feb 9, 2005
 *
 */
package dk.diku.emerald;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


class EmeraldErrorMessage implements Runnable {

		private String message = "";
        private String title = "";

        public EmeraldErrorMessage(String title, String message){
		    this.title = title;
		    this.message = message;
		}

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            MessageDialog.openError(new Shell(),title,message);
        }

}