package rabbitescape.engine.util;

import static rabbitescape.engine.util.Util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealFileSystem implements FileSystem
{
    @Override
    public boolean exists( String fileName )
    {
        return new File( fileName ).exists();
    }

    @Override
    public String[] readLines( String fileName )
        throws FileNotFoundException, IOException
    {
        List<String> ret = new ArrayList<>();

        try (
            BufferedReader reader = new BufferedReader(
                new FileReader( new File( fileName ) )
            )
        )
        {
            String line = reader.readLine();

            while ( line != null )
            {
                ret.add( line );
                line = reader.readLine();
            }

            return stringArray( ret );
        }
    }

    @Override
    public String read( String fileName )
        throws FileNotFoundException, IOException
    {
        return join( "\n", readLines( fileName ) );
    }

    @Override
    public void write( String fileName, String contents ) throws IOException
    {
        try ( FileWriter writer = new FileWriter( new File( fileName ) ) )
        {
            writer.append( contents );
        }
    }

    @Override
    public String parent( String filePath )
    {
        return new File( filePath ).getParent();
    }

    @Override
    public void mkdirs( String dirPath )
    {
        new File( dirPath ).mkdirs();
    }
}
