/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014,2015 Dennis Sheirer
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package io.github.dsheirer.module.decode.lj1200;

import io.github.dsheirer.alias.AliasList;
import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.message.Message;
import io.github.dsheirer.sample.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LJ1200MessageProcessor implements Listener<BinaryMessage>
{
	private final static Logger mLog = 
			LoggerFactory.getLogger( LJ1200MessageProcessor.class );

	public static int[] SYNC = { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 };

	public static int SYNC_TOWER = 0x550F;
	public static int SYNC_TRANSPONDER = 0x2AD5;

	private Listener<Message> mMessageListener;
	
	private AliasList mAliasList;
	
	public LJ1200MessageProcessor( AliasList list )
	{
		mAliasList = list;
	}
	
	public void dispose()
	{
		mAliasList = null;
		mMessageListener = null;
	}
	
	@Override
    public void receive( BinaryMessage buffer )
    {
		int sync = buffer.getInt( SYNC );

		if( sync == SYNC_TOWER )
		{
			if( mMessageListener != null )
			{
				mMessageListener.receive( new LJ1200Message( buffer, mAliasList ) );
			}
		}
		else if( sync == SYNC_TRANSPONDER )
		{
			if( mMessageListener != null )
			{
				mMessageListener.receive( new LJ1200TransponderMessage( buffer, mAliasList ) );
			}
		}
    }
	
    public void setMessageListener( Listener<Message> listener )
    {
		mMessageListener = listener;
    }

    public void removeMessageListener()
    {
		mMessageListener = null;
    }
}
