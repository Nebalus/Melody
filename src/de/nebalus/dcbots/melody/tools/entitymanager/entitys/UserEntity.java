package de.nebalus.dcbots.melody.tools.entitymanager.entitys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.datamanager.files.LiteSQL;
import de.nebalus.dcbots.melody.tools.entitymanager.DatabaseValueContainer;
import de.nebalus.dcbots.melody.tools.entitymanager.Entity;
import net.dv8tion.jda.api.entities.User;

public final class UserEntity extends Entity
{
    private final Long userid;

    private final LiteSQL database = Melody.getDatabase();

    public UserEntity(Long userid)
    {
        this.userid = userid;

        if(database.isConnected())
        {
            try 
			{
				for (UserEntityDBOptions option : UserEntityDBOptions.values()) 
				{
					final DatabaseValueContainer dvc = new DatabaseValueContainer(option.name(), option.canbeexported, option.defaultvalue);
					createDatabaseValueContainer(dvc);
				}
				
                ResultSet rsuser = database.onQuery("SELECT * FROM userdata WHERE PK_userid = " + userid);
				if(rsuser.next())
                {
                    for(UserEntityDBOptions option : UserEntityDBOptions.values())
                    {
                        final DatabaseValueContainer dvc = getDatabaseValueContainer(option.name());
						final String databasename = option.databasename;	

                        if (rsuser.getString(databasename) != null) 
						{
							switch (option) {
								case FIRSTTIMELOADED:
									dvc.updateValue(rsuser.getLong(databasename), true);
                                    break;

								case FIRSTTIMEHEARD:
                                	dvc.updateValue(rsuser.getLong(databasename), true);
                                    break;
                                    
								case LASTTIMELOADED:
                                	dvc.updateValue(rsuser.getLong(databasename), true);
                                    break;               
                                    
                                case LASTTIMEHEARD:
                                	dvc.updateValue(rsuser.getLong(databasename), true);
                                    break;

                                case FAVORITPLAYLISTID:
                                	dvc.updateValue(rsuser.getInt(databasename), true);
                                    break;
                                    
                                case HEARDTIME:
                                	dvc.updateValue(rsuser.getLong(databasename), true);
                                    break;
							default:
								break;
							}
						}
                    }
                }
				else 
				{
					ConsoleLogger.info("UserEntity", "Loading USERID:" + userid + " for the first time!");
					// loads the guild in the database
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO userdata(PK_userid, " + UserEntityDBOptions.FIRSTTIMELOADED.databasename + ") VALUES(?,?)");
					ps.setLong(1, userid);
					ps.setLong(2, System.currentTimeMillis());
					ps.executeUpdate();
				}
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        else 
		{
			throw new NullPointerException("The Database is unreachable!");
		}
    }

    private enum UserEntityDBOptions {
    	FIRSTTIMELOADED(System.currentTimeMillis(), "firsttimeloaded", false),
        FIRSTTIMEHEARD(System.currentTimeMillis(), "firsttimeheard", false),
        LASTTIMELOADED(System.currentTimeMillis(), "lasttimeloaded", true),
        LASTTIMEHEARD(System.currentTimeMillis(), "lasttimeheard", true),
        FAVORITPLAYLISTID(0, "FK_favoriteplaylistid", true),
        HEARDTIME(0, "heardtime", true);

        final Object defaultvalue;
		final String databasename;
		final boolean canbeexported;

		UserEntityDBOptions (Object defaultvalue, String databasename, boolean canbeexported) {
			this.defaultvalue = defaultvalue;
			this.databasename = databasename;
			this.canbeexported = canbeexported;
		}
    }
    
    public Long getUserId() {
		renewExpireTime();
		return this.userid;
	}
	
	public User getUser() {
		renewExpireTime();
		return Melody.getUserById(userid);
	}
	

}