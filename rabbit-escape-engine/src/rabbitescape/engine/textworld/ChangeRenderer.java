package rabbitescape.engine.textworld;

import rabbitescape.engine.ChangeDescription;
import rabbitescape.engine.ChangeDescription.Change;
import rabbitescape.engine.RabbitStates;
import rabbitescape.engine.StateAndPosition;
import rabbitescape.engine.util.Position;

public class ChangeRenderer
{
    public static void render( Chars chars, ChangeDescription desc )
    {
        for ( ChangeDescription.Change change : desc.changes )
        {
            charForChange( change, chars );
        }
    }

    private static void charForChange( Change change, Chars chars )
    {
        // Handle bridging specially
        Position bridgingPos = RabbitStates.whereBridging(
            new StateAndPosition( change.state, change.x, change.y ) );

        if ( bridgingPos != null )
        {
            chars.set(
                bridgingPos.x,
                bridgingPos.y,
                RabbitStates.bridgingStage( change.state )
            );
            return;
        }

        // Everything else is relatively simple
        switch( change.state )
        {
            case NOTHING:
                break;
            case RABBIT_WALKING_LEFT:
                chars.set( change.x-1, change.y, '<' );
                break;
            case RABBIT_TURNING_LEFT_TO_RIGHT:
                chars.set( change.x, change.y, '|' );
                break;
            case RABBIT_TURNING_LEFT_TO_RIGHT_RISING:
                chars.set( change.x, change.y, '|' );
                break;
            case RABBIT_TURNING_LEFT_TO_RIGHT_LOWERING:
                chars.set( change.x, change.y, '[' );
                break;
            case RABBIT_WALKING_RIGHT:
                chars.set( change.x + 1, change.y, '>' );
                break;
            case RABBIT_TURNING_RIGHT_TO_LEFT:
                chars.set( change.x, change.y, '?' );
                break;
            case RABBIT_TURNING_RIGHT_TO_LEFT_RISING:
                chars.set( change.x, change.y, '?' );
                break;
            case RABBIT_TURNING_RIGHT_TO_LEFT_LOWERING:
                chars.set( change.x, change.y, ']' );
                break;
            case RABBIT_RISING_RIGHT_START:
                chars.set( change.x + 1, change.y, '~' );
                break;
            case RABBIT_RISING_RIGHT_CONTINUE:
                chars.set( change.x + 1, change.y - 1, '$' );
                break;
            case RABBIT_RISING_RIGHT_END:
                chars.set( change.x + 1, change.y - 1, '\'' );
                break;
            case RABBIT_RISING_LEFT_START:
                chars.set( change.x - 1, change.y, '`' );
                break;
            case RABBIT_RISING_LEFT_CONTINUE:
                chars.set( change.x - 1, change.y - 1, '^' );
                break;
            case RABBIT_RISING_LEFT_END:
                chars.set( change.x - 1, change.y - 1, '!' );
                break;
            case RABBIT_LOWERING_RIGHT_START:
                chars.set( change.x + 1, change.y + 1, '-' );
                break;
            case RABBIT_LOWERING_RIGHT_CONTINUE:
                chars.set( change.x + 1, change.y + 1, '@' );
                break;
            case RABBIT_LOWERING_RIGHT_END:
                chars.set( change.x + 1, change.y, '_' );
                break;
            case RABBIT_LOWERING_LEFT_START:
                chars.set( change.x - 1, change.y + 1, '=' );
                break;
            case RABBIT_LOWERING_LEFT_CONTINUE:
                chars.set( change.x - 1, change.y + 1, '%' );
                break;
            case RABBIT_LOWERING_LEFT_END:
                chars.set( change.x - 1, change.y, '+' );
                break;
            case RABBIT_LOWERING_AND_RISING_RIGHT:
                chars.set( change.x + 1, change.y, ',' );
                break;
            case RABBIT_LOWERING_AND_RISING_LEFT:
                chars.set( change.x - 1, change.y, '.' );
                break;
            case RABBIT_RISING_AND_LOWERING_RIGHT:
                chars.set( change.x + 1, change.y, '&' );
                break;
            case RABBIT_RISING_AND_LOWERING_LEFT:
                chars.set( change.x - 1, change.y, 'm' );
                break;
            case RABBIT_FALLING:
                chars.set( change.x, change.y + 1, 'f' );
                chars.set( change.x, change.y + 2, 'f' );
                break;
            case RABBIT_FALLING_ONTO_LOWER_RIGHT:
                chars.set( change.x, change.y + 1, 'f' );
                chars.set( change.x, change.y + 2, 'e' );
                break;
            case RABBIT_FALLING_ONTO_LOWER_LEFT:
                chars.set( change.x, change.y + 1, 'f' );
                chars.set( change.x, change.y + 2, 's' );
                break;
            case RABBIT_FALLING_ONTO_RISE_RIGHT:
                chars.set( change.x, change.y + 1, 'f' );
                chars.set( change.x, change.y + 2, 'h' );
                break;
            case RABBIT_FALLING_ONTO_RISE_LEFT:
                chars.set( change.x, change.y + 1, 'f' );
                chars.set( change.x, change.y + 2, 'a' );
                break;
            case RABBIT_FALLING_1:
                chars.set( change.x, change.y + 1, 'f' );
                break;
            case RABBIT_FALLING_1_ONTO_LOWER_RIGHT:
                chars.set( change.x, change.y + 1, 'e' );
                break;
            case RABBIT_FALLING_1_ONTO_LOWER_LEFT:
                chars.set( change.x, change.y + 1, 's' );
                break;
            case RABBIT_FALLING_1_ONTO_RISE_RIGHT:
                chars.set( change.x, change.y + 1, 'h' );
                break;
            case RABBIT_FALLING_1_ONTO_RISE_LEFT:
                chars.set( change.x, change.y + 1, 'a' );
                break;
            case RABBIT_FALLING_1_TO_DEATH:
                chars.set( change.x, change.y + 1, 'x' );
                break;
            case RABBIT_DYING_OF_FALLING_2:
                chars.set( change.x, change.y, 'y' );
                break;
            case RABBIT_DYING_OF_FALLING:
                chars.set( change.x, change.y, 'X' );
                break;
            case RABBIT_ENTERING_EXIT:
                chars.set( change.x, change.y, 'R' );
                break;
            case RABBIT_BASHING_RIGHT:
                chars.set( change.x + 1, change.y, 'K' );
                break;
            case RABBIT_BASHING_LEFT:
                chars.set( change.x - 1, change.y, 'W' );
                break;
            case RABBIT_BASHING_UP_RIGHT:
                chars.set( change.x + 1, change.y - 1, 'K' );
                break;
            case RABBIT_BASHING_UP_LEFT:
                chars.set( change.x - 1, change.y - 1, 'W' );
                break;
            case RABBIT_BASHING_USELESSLY_RIGHT:
                chars.set( change.x + 1, change.y, 'I' );
                break;
            case RABBIT_BASHING_USELESSLY_LEFT:
                chars.set( change.x - 1, change.y, 'J' );
                break;
            case RABBIT_CLIMBING_LEFT_START:
                chars.set( change.x, change.y, 'T' );
                break;
            case RABBIT_CLIMBING_LEFT_CONTINUE_1:
            case RABBIT_CLIMBING_LEFT_CONTINUE_2:
                chars.set( change.x, change.y - 1, 'Y' );
                break;
            case RABBIT_CLIMBING_LEFT_END:
                chars.set( change.x - 1, change.y - 1, 'U' );
                break;
            case RABBIT_CLIMBING_LEFT_BANG_HEAD:
                chars.set( change.x, change.y, 'Y' );
                break;
            case RABBIT_CLIMBING_RIGHT_START:
                chars.set( change.x, change.y, 'G' );
                break;
            case RABBIT_CLIMBING_RIGHT_CONTINUE_1:
            case RABBIT_CLIMBING_RIGHT_CONTINUE_2:
                chars.set( change.x, change.y - 1, 'F' );
                break;
            case RABBIT_CLIMBING_RIGHT_END:
                chars.set( change.x + 1, change.y - 1, 'L' );
                break;
            case RABBIT_CLIMBING_RIGHT_BANG_HEAD:
                chars.set( change.x, change.y, 'F' );
                break;
            case RABBIT_DIGGING:
                chars.set( change.x, change.y + 1, 'D' );
                break;
            case RABBIT_DIGGING_ON_SLOPE:
                chars.set( change.x, change.y, 'D' );
                break;
            case RABBIT_DIGGING_2:
                chars.set( change.x, change.y, 'D' );
                break;
            case RABBIT_BLOCKING:
                chars.set( change.x, change.y, 'H' );
                break;
            case RABBIT_EXPLODING:
                chars.set( change.x, change.y, 'P' );
                break;
            case TOKEN_BASH_STILL:
            case TOKEN_DIG_STILL:
            case TOKEN_BRIDGE_STILL:
            case TOKEN_BLOCK_STILL:
            case TOKEN_CLIMB_STILL:
            case TOKEN_EXPLODE_STILL:
                break;
            case TOKEN_BASH_FALLING:
            case TOKEN_DIG_FALLING:
            case TOKEN_BRIDGE_FALLING:
            case TOKEN_BLOCK_FALLING:
            case TOKEN_CLIMB_FALLING:
            case TOKEN_EXPLODE_FALLING:
                chars.set( change.x, change.y + 1, 'f' );
                break;
            case ENTRANCE:
                break;
            case EXIT:
                break;
            default:
                throw new AssertionError(
                    "Unknown Change state: " + change.state.name() );
        }
    }
}
