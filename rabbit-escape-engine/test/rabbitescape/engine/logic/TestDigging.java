package rabbitescape.engine.logic;

import static org.hamcrest.MatcherAssert.*;
import static rabbitescape.engine.Tools.*;
import static rabbitescape.engine.textworld.TextWorldManip.*;
import static rabbitescape.engine.util.WorldAssertions.*;

import org.junit.Test;

import rabbitescape.engine.World;

public class TestDigging
{
    @Test
    public void Dig_through_single_floor()
    {
        World world = createWorld(
            "rd ",
            "###",
            "   ",
            "   ",
            "###"
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                " r ",
                "#D#",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#D#",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#r#",
                " f ",
                " f ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "   ",
                " r>",
                "###"
            )
        );
    }

    @Test
    public void Dig_through_multilevel_floor()
    {
        World world = createWorld(
            "rd ",
            "###",
            "###",
            "###",
            "   ",
            "   ",
            "###"
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                " r ",
                "#D#",
                "###",
                "###",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#D#",
                "###",
                "###",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#r#",
                "#D#",
                "###",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "#D#",
                "###",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "#r#",
                "#D#",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "#D#",
                "   ",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "#r#",
                " f ",
                " f ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "# #",
                "   ",
                " r>",
                "###"
            )
        );
    }

    @Test
    public void Stop_after_single_gap()
    {
        World world = createWorld(
            "rd ",
            "###",
            "   ",
            "###"
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                " r ",
                "#D#",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#D#",
                "   ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "#r#",
                " f ",
                "###"
            )
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                " r>",
                "###"
            )
        );
    }

    @Test
    public void Stop_after_single_slope()
    {
        assertWorldEvolvesLike(
            " dj" + "\n" +
            " /#" + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " D#" + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " j#" + "\n" +
            " f " + "\n" +
            "###",

            "   " + "\n" +
            "  #" + "\n" +
            "<j " + "\n" +
            "###"
        );
    }

    @Test
    public void Stop_after_single_gap_after_multilevel_dig()
    {
        World world = createWorld(
            "rd ",
            "###",
            "###",
            "###",
            "   ",
            "###"
        );

        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                " r ",
                "#D#",
                "###",
                "###",
                "   ",
                "###"
            )
        );

        world.step();
        world.step();
        world.step();
        world.step();
        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "#D#",
                "   ",
                "###"
            )
        );
        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "#r#",
                " f ",
                "###"
            )
        );
        world.step();

        assertThat(
            renderWorld( world, true, false ),
            equalTo(
                "   ",
                "# #",
                "# #",
                "# #",
                " r>",
                "###"
            )
        );
    }

    @Test
    public void Dig_through_single_slope()
    {
        assertWorldEvolvesLike(
            " r " + "\n" +
            " * " + "\n" +
            "   " + "\n" +
            "###" + "\n" +
            ":*=d/",

            "   " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " r " + "\n" +
            " f " + "\n" +
            "###"
        );
    }

    @Test
    public void Dig_through_single_bridge()
    {
        assertWorldEvolvesLike(
            " r " + "\n" +
            " * " + "\n" +
            "   " + "\n" +
            "###" + "\n" +
            ":*=d(",

            "   " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " r " + "\n" +
            " f " + "\n" +
            "###"
        );
    }

    @Test
    public void Dig_through_slope_plus_blocks()
    {
        assertWorldEvolvesLike(
            " r " + "\n" +
            " * " + "\n" +
            " # " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###" + "\n" +
            ":*=d/",

            "   " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " r " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            " r " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            "   " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            "   " + "\n" +
            " r " + "\n" +
            " f " + "\n" +
            "###"
        );
    }

    @Test
    public void Dig_through_bridge_plus_blocks()
    {
        assertWorldEvolvesLike(
            " r " + "\n" +
            " * " + "\n" +
            " # " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###" + "\n" +
            ":*=d(",

            "   " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " r " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            " D " + "\n" +
            " # " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            " r " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            "   " + "\n" +
            " D " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "   " + "\n" +
            "   " + "\n" +
            " r " + "\n" +
            " f " + "\n" +
            "###"
        );
    }

    @Test
    public void Dig_through_bridge_plus_bridges()
    {
        assertWorldEvolvesLike(
            " r " + "\n" +
            " * " + "\n" +
            " ( " + "\n" +
            " ( " + "\n" +
            "   " + "\n" +
            "###" + "\n" +
            ":*=d(",

            "   " + "\n" +
            " D " + "\n" +
            " ( " + "\n" +
            " ( " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            " r " + "\n" +
            " h " + "\n" +
            " ( " + "\n" +
            "   " + "\n" +
            "###",

            "   " + "\n" +
            "  '" + "\n" +
            " r " + "\n" + // Starts walking
            " ( " + "\n" +
            "   " + "\n" +
            "###"
        );
    }

    @Test
    public void Bash_stops_you_digging()
    {
        assertWorldEvolvesLike(
            " bb " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "*dd " + "\n" +
            "### " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            ":*=rr",

            "    " + "\n" +
            " bb " + "\n" +
            " ff " + "\n" +
            "    " + "\n" +
            " r> " + "\n" +
            "#D# " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            " bb " + "\n" +
            " ff " + "\n" +
            "  r " + "\n" +
            "#DD " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            " bb " + "\n" +
            " ff " + "\n" +
            "#rD " + "\n" +
            " D# " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            " bb " + "\n" +
            "#ff " + "\n" +
            " DD " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#bb " + "\n" +
            " ff " + "\n" +
            " D# " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            " brI" + "\n" +
            " f# " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            "  r>" + "\n" +
            " rK " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            "   r" + "\n" +
            " r>f" + "\n" +
            " ##f" + "\n" +
            " ## "
        );
    }

    @Test
    public void Bridge_stops_you_digging()
    {
        assertWorldEvolvesLike(
            " ii " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "*dd " + "\n" +
            "### " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            ":*=rr",

            "    " + "\n" +
            " ii " + "\n" +
            " ff " + "\n" +
            "    " + "\n" +
            " r> " + "\n" +
            "#D# " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            " ii " + "\n" +
            " ff " + "\n" +
            "  r " + "\n" +
            "#DD " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            " ii " + "\n" +
            " ff " + "\n" +
            "#rD " + "\n" +
            " D# " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            " ii " + "\n" +
            "#ff " + "\n" +
            " DD " + "\n" +
            " ## " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#ii " + "\n" +
            " ff " + "\n" +
            " D# " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            " irB" + "\n" +
            " f# " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            "  r[" + "\n" +
            " B# " + "\n" +
            " ## " + "\n" +
            " ## ",

            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "    " + "\n" +
            "#   " + "\n" +
            "  r{" + "\n" +
            " [# " + "\n" +
            " ## " + "\n" +
            " ## "
        );
    }
}
