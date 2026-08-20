package genetic_algorithm;

import java.util.Random;

public abstract class Chromosome {

    private static final Random random = new Random();
    public abstract void display();
    public abstract Object getGene(int index);
    public abstract void setGene(int index, Object value);
    public abstract void mutateGene(int index);
    public abstract Chromosome copy();
    private int size;

    public int getSize() {
        return size;
    }

    public static class Int extends Chromosome {
        private int[] genes;
        private int min;
        private int max;

        public Int(int size, int min, int max) {
            super.size = size;
            this.min = min;
            this.max = max;
            genes = new int[size];
            for (int i = 0; i < size; i++) {
                setGene(i, randomValue(this.min, this.max));
            }
        }

        public Int(int[] genes) {
            this.genes = genes.clone();
            super.size = genes.length;
        }

        public int[] getGenes() {
            return genes;
        }

        @Override
        public Object getGene(int index){
            return genes[index];
        }

        @Override
        public void setGene(int index, Object value) {
            genes[index] = (int) value;
        }
        @Override
        public void mutateGene(int index){
            setGene(index, randomValue(this.min, this.max));
        }
        @Override
        public Chromosome copy() {
            return new Int(genes);
        }

        public static int randomValue(int min, int max) {
            return random.nextInt(max - min + 1) + min;
        }

        @Override
        public void display() {
            System.out.print("{ ");
            for (int gene : genes) {
                System.out.print(gene + " ");
            }
            System.out.println("}");
        }
    }

    public static class Permutation extends Chromosome {
        private int[] genes;

        public Permutation(int size) {
            super.size = size;
            genes = new int[size];

            for (int i = 0; i < size; i++) genes[i] = i;

            for (int i = size - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = genes[i];
                genes[i] = genes[j];
                genes[j] = temp;
            }
        }

        public Permutation(int[] genes) {
            this.genes = genes.clone();
            super.size = genes.length;
        }

        public int[] getGenes() {
            return genes;
        }

        @Override
        public Object getGene(int index) {
            return genes[index];
        }

        @Override
        public void setGene(int index, Object value) {
            genes[index] = (int) value;
        }

        @Override
        public void mutateGene(int index) {
            int otherIndex = random.nextInt(genes.length);
            int temp = genes[index];
            genes[index] = genes[otherIndex];
            genes[otherIndex] = temp;
        }

        @Override
        public Chromosome copy() {
            return new Permutation(genes);
        }

        @Override
        public void display() {
            System.out.print("{ ");
            for (int gene : genes) System.out.print(gene + " ");
            System.out.println("}");
        }
    }

    public static class Float extends Chromosome{
        private float[] genes;
        private float min;
        private float max;

        public Float(int size, float min, float max) {
            super.size = size;
            genes = new float[size];
            for (int i = 0; i < size; i++) {
                setGene(i, randomValue(this.min, this.max));
            }
        }

        public Float(float[] genes) {
            this.genes = genes.clone();
            super.size = genes.length;
        }

        public float[] getGenes() {
            return genes;
        }

        @Override
        public Object getGene(int index){
            return genes[index];
        }

        @Override
        public void setGene(int index, Object value) {
            genes[index] = (float) value;
        }

        @Override
        public void mutateGene(int index){
            setGene(index, randomValue(this.min, this.max));
        }
        @Override
        public Chromosome copy() {
            return new Float(genes);
        }

        public static float randomValue(float min, float max) {
            return min + random.nextFloat() * (max - min);
        }
        @Override
        public void display() {
            System.out.print("{ ");
            for (float gene : genes) {
                System.out.print(gene + " ");
            }
            System.out.println("}");
        }
    }

    public static class Double extends Chromosome{
        private double[] genes;
        private double min;
        private double max;

        public Double(int size, double min, double max) {
            super.size = size;
            genes = new double[size];
            for (int i = 0; i < size; i++) {
                setGene(i, randomValue(this.min, this.max));
            }
        }

        public Double(double[] genes) {
            this.genes = genes.clone();
            super.size = genes.length;
        }

        public double[] getGenes() {
            return genes;
        }

        @Override
        public Object getGene(int index){
            return genes[index];
        }

        @Override
        public void setGene(int index, Object value) {
            genes[index] = (double) value;
        }

        @Override
        public void mutateGene(int index){
            setGene(index, randomValue(this.min, this.max));
        }
        @Override
        public Chromosome copy() {
            return new Double(genes);
        }

        public static double randomValue(double min, double max) {
            return min + random.nextDouble() * (max - min);
        }

        @Override
        public void display() {
            System.out.print("{ ");
            for (double gene : genes) {
                System.out.print(gene + " ");
            }
            System.out.println("}");
        }
    }

    public static class Boolean extends Chromosome{
        private boolean[] genes;

        public Boolean(int size) {
            super.size = size;
            genes = new boolean[size];
            for (int i = 0; i < size; i++) {
                genes[i] = random.nextBoolean();
            }
        }

        public Boolean(boolean[] genes) {
            this.genes = genes.clone();
            super.size = genes.length;
        }

        public boolean[] getGenes() {
            return genes;
        }

        @Override
        public Object getGene(int index){
            return genes[index];
        }

        @Override
        public void setGene(int index, Object value) {
            genes[index] = (boolean) value;
        }

        @Override
        public void mutateGene(int index){
            setGene(index, randomValue());
        }

        @Override
        public Chromosome copy() {
            return new Boolean(genes);
        }

        public static boolean randomValue() {
            return random.nextBoolean();
        }

        @Override
        public void display() {
            System.out.print("{ ");
            for (boolean gene : genes) {
                System.out.print(gene + " ");
            }
            System.out.println("}");
        }
    }
}