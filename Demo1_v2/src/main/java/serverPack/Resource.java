package serverPack;


/**
* The Resource class represents a resource entity with properties like ID, name, breed, and age.
*/

public class Resource {

    private String id;
    private String name;
    private String breed;
    private int age;

    
	/**
	 * Constructs a new Resource object with the specified ID, name, breed, and age.
	 *
	 * @param id    The ID of the resource.
	 * @param name  The name of the resource.
	 * @param breed The breed of the resource.
	 * @param age   The age of the resource.
	 */
    
    public Resource(String id, String name, String breed, int age) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    
    /**
     * Returns a string representation of the Resource object.
     *
     * @return A string representing the Resource object's ID, name, breed, and age.
     */
    
    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                '}';
    }
}
