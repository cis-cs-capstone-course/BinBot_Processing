package edu.temple.capstone.BinBotServer.instructions;

import com.google.gson.Gson;
import edu.temple.capstone.BinBotServer.PatrolSequence;
import edu.temple.capstone.BinBotServer.WasteDetector;
import edu.temple.capstone.BinBotServer.data.Prediction;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;


/**
 * The Instruction class represents a set of instructions that BinBot should follow in order to retrieve trash. It is
 * also capable of converting json string to this object and providing a json version of itself in the format of
 * {
 * "status":<"PATROL"|"NAVIGATION"|"RETRIEVAL">,
 * "img":<image as object>,
 * "treads:[
 * {
 * "angle":<Double>,
 * "distance":<Double>
 * }
 * ]
 * "arms":[
 * {
 * "angle":<Double>
 * }
 * ]
 * }
 * where status is BinBot's operating status, img holds a picture captured by BinBot's camera, treads is an array
 * containing pairs of angles it should turn and distances it should travel forward after turning,
 * and arms is an array of angles each joint should turn.
 *
 * @author Sean DiGirolamo, Sean Reddington
 * @since 2019-10-18
 */
public class Instruction {
    private Status status;
    private BufferedImage img;
    private List<Movement> treads;
    private List<Double> arms;
    public Double distance;

    /**
     * This constructor takes as input a json string. It assumes that the json is properly formatted in the proper
     * configuration and results in an Instruction object based on the json string provided.
     *
     * @author Sean DiGirolamo
     * @since 2019-10-18
     */
    public Instruction(String json) {
        Gson gson = new Gson();
        GsonInstruction g = gson.fromJson(json, GsonInstruction.class);
        this.status = Status.valueOf(g.status);
        this.img = this.stringToBufferedImage(g.img);
        this.treads = g.treads;
        this.arms = g.arms;
//        JSONObject jsonObject = new JSONObject(json);
//
//        // Parse image first and remove from jsonObject to improve efficiency
//        this.img = this.stringToBufferedImage(jsonObject.getString("img"));
//        jsonObject.remove("img");
//
//        this.status = Status.valueOf(jsonObject.getString("status"));
//
////        this.treads = new ArrayList<>();
//        this.distance = jsonObject.getDouble("treads");
//
//
////        for (Object o : jsonObject.getJSONArray("treads")) {
////            JSONObject jo = (JSONObject) o;
////			treads.add(new Movement(jo.getDouble("angle"), jo.getDouble("distance")));
////        }
////
////        this.arms = new ArrayList<>();
////        for (Object o : jsonObject.getJSONArray("arms")) {
////            arms.add(((JSONObject) o).getDouble("angle"));
////        }
    }

    public Instruction generateInstruction(WasteDetector wasteDetector, PatrolSequence patrolSequence) {
        Status status = null;
        List<Movement> treads = new ArrayList<>();
        Movement movement = null;

        List<Prediction> preds = wasteDetector.getPredictions();
        if (preds == null || preds.isEmpty()) {
            status = Status.PATROL;
            treads.add(patrolSequence.next());
        } else {
            Prediction p = preds.get(0);
            treads = TreadInstruction.calcInstructions(p.getUpperLeftX(), p.getCenterX(),
                    p.getUpperLeftY(), p.getCenterY(),
                    p.getWidth(), p.getHeight(),
                    p.getParentImageWidth(), p.getParentImageHeight(), this.distance
            );
            movement = treads.get(0);
            if (movement.angle() == 0.0 && movement.distance() == 1.0) { // in range of arm
                status = Status.RETRIEVE;
            } else {
                status = Status.MOVE;
            }

            patrolSequence.reset();
        }

        return new Instruction(status, wasteDetector.getBufferedImage(), treads, null);
    }

    /**
     * This constructor takes as input an object array generated by OpenCV. Contained in this array is data about where
     * waste has been located and if waste is in the image. Based on this data, instructions will be calculated for
     * Binbot to execute and placed inside the resulting Instruction object.
     *
     * @author Sean DiGirolamo
     * @since 2019-10-18
     */
    public Instruction(Object[][] o) {
        this.status = Status.PATROL;
        this.img = null;
        this.treads = new ArrayList<>();
        this.treads.add(new Movement(0.0, 00.0));
        this.arms = new ArrayList<>();
        this.arms = new ArrayList<>();
        this.arms.add(0.0);
    }

    /**
     * This method returns the Instruction class a json string which can be sent to the BinBot robot and interpreted
     * as a set of commands to follow in the format described above.
     *
     * @author Sean DiGirolamo
     * @since 2019-10-20
     */
    public String json() {
        StringBuilder retval = new StringBuilder("{\"status\":\"")
                .append(this.status.toString())
                .append("\",")
                .append("\"img\":" + "\"");

        if (this.img != null) {
            retval.append(bufferedImageToString(img));
        }

        retval.append("\",")
                .append("\"treads\":[");

        if (this.treads != null) {
            for (Movement movement : this.treads) {
                retval.append("{\"angle\":").append(movement.angle()).append(",");
                retval.append("\"distance\":").append(movement.distance()).append("}");
                if (movement != this.treads.get(this.treads.size() - 1)) {
                    retval.append(",");
                }
            }
        }

        retval.append("],")
                .append("\"arms\":[");

        if (this.arms != null) {
            for (Double d : this.arms) {
                retval.append("{\"angle\":")
                        .append(d)
                        .append("}");
                if (!d.equals(this.arms.get(this.arms.size() - 1))) {
                    retval.append(",");
                }
            }
        }

        retval.append("]}");

        return retval.toString();
    }

    /**
     * This method creates a new Instruction object based on the arguments provided. This shouldn't really ever be used
     * outside of testing purposes, but it is here just in case.
     *
     * @author Sean DiGirolamo
     * @since 2019-10-23
     */
    public Instruction(Status status, BufferedImage img, List<Movement> treads, List<Double> arms) {
        this.status = status;
        this.img = img;
        this.treads = treads;
        this.arms = arms;
    }

    /**
     * This method returns the Buffered img contained within the Instruction
     *
     * @author Sean DiGirolamo
     * @since 2019-10-21
     */
    public BufferedImage getImage() {
        return this.img;
    }

    /**
     * This method takes a base64 encoded string of a jpeg image and decodes it to a returned buffered image.
     *
     * @author Sean Reddington
     * @since 2019-10-25
     */
    private BufferedImage stringToBufferedImage(String s) {
        System.out.println(s);
        BufferedImage bufferedImage = null;
        if (s != null) {
            byte[] bytes = Base64.getDecoder().decode(s);
            try {
                bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bufferedImage;
    }

    /**
     * This method takes a BufferedImage object and encodes it to a returned base64 string.
     *
     * @author Sean Reddington
     * @since 2019-10-25
     */
    private String bufferedImageToString(BufferedImage bi) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = out.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);

    }

    /**
     * This method returns this status contained in the Instruction.
     *
     * @author Sean DiGirolamo
     * @since 2019-11-13
     */
    public Status status() {
        return this.status;
    }
}
