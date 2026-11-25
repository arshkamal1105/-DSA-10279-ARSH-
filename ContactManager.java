import java.io.*;
import java.util.*;

class Contact {
    String id, name, phone, email;
    Contact(String id, String name, String phone, String email) {
        this.id = id; this.name = name; this.phone = phone; this.email = email;
    }
    public String toString() {
        return id + " | " + name + " | " + phone + (email.isEmpty() ? "" : " | " + email);
    }
}

class Node {                       // BST node (index on name)
    Contact c; Node left, right;
    Node(Contact c) { this.c = c; }
}

public class ContactManager {
    static LinkedList<Contact> list = new LinkedList<>();          // storage
    static HashMap<String, Contact> phoneMap = new HashMap<>();    // phone index
    static Node root;                                              // name index (BST)
    static int nextId = 101;
    static Scanner sc = new Scanner(System.in);

    static Node insert(Node n, Contact c) {
        if (n == null) return new Node(c);
        if (c.name.compareToIgnoreCase(n.c.name) < 0) n.left = insert(n.left, c);
        else n.right = insert(n.right, c);
        return n;
    }

    static void inorder(Node n) {
        if (n == null) return;
        inorder(n.left); System.out.println(n.c); inorder(n.right);
    }

    static boolean searchName(Node n, String name) {
        if (n == null) return false;
        int r = name.compareToIgnoreCase(n.c.name);
        boolean found = false;
        if (r <= 0) found |= searchName(n.left, name);
        if (r == 0) { System.out.println(n.c); found = true; }
        if (r >= 0) found |= searchName(n.right, name);
        return found;
    }

    // rebuild both indexes from the list -> always consistent after update/delete
    static void rebuild() {
        root = null; phoneMap.clear();
        for (Contact c : list) { root = insert(root, c); phoneMap.put(c.phone, c); }
    }

    static String ask(String msg) { System.out.print(msg); return sc.nextLine().trim(); }

    static Contact find(String id) {
        for (Contact c : list) if (c.id.equalsIgnoreCase(id)) return c;
        return null;
    }

    static boolean phoneOk(String p, Contact self) {
        if (!p.matches("\\d{10}")) { System.out.println("Invalid phone. Must be exactly 10 digits."); return false; }
        Contact other = phoneMap.get(p);
        if (other != null && other != self) { System.out.println("Duplicate phone number."); return false; }
        return true;
    }

    static void add(String name, String phone, String email) {
        if (!phoneOk(phone, null)) return;
        Contact c = new Contact("C" + nextId++, name.replace(",", " "), phone, email.replace(",", " "));
        list.add(c); rebuild();
        System.out.println("Contact added: " + c);
    }

    static void update() {
        Contact c = find(ask("Enter ID: "));
        if (c == null) { System.out.println("Contact not found."); return; }
        String name = ask("New name (Enter to skip): ");
        String phone = ask("New phone (Enter to skip): ");
        String email = ask("New email (Enter to skip): ");
        if (!phone.isEmpty()) { if (!phoneOk(phone, c)) return; c.phone = phone; }
        if (!name.isEmpty()) c.name = name.replace(",", " ");
        if (!email.isEmpty()) c.email = email.replace(",", " ");
        rebuild();
        System.out.println("Contact updated: " + c);
    }

    static void delete() {
        Contact c = find(ask("Enter ID: "));
        if (c == null) { System.out.println("Contact not found."); return; }
        list.remove(c); rebuild();
        System.out.println("Contact deleted.");
    }

    static void sort(boolean desc) {
        List<Contact> a = new ArrayList<>(list);
        a.sort((x, y) -> x.name.compareToIgnoreCase(y.name));
        if (desc) Collections.reverse(a);
        for (int i = 0; i < a.size(); i++) System.out.println((i + 1) + ". " + a.get(i));
    }

    static void load() {
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                list.add(new Contact(p[0], p[1], p[2], p[3]));
                nextId = Math.max(nextId, Integer.parseInt(p[0].substring(1)) + 1);
            }
        } catch (Exception e) { /* no file yet */ }
        rebuild();
    }

    static void save() {
        try (PrintWriter pw = new PrintWriter("contacts.txt")) {
            for (Contact c : list) pw.println(c.id + "," + c.name + "," + c.phone + "," + c.email);
        } catch (Exception e) { System.out.println("Save error: " + e.getMessage()); }
    }

    public static void main(String[] args) {
        load();
        while (true) {
            System.out.println("\n1.Add 2.Display 3.Search Name 4.Search Phone 5.Update 6.Delete 7.Sort 8.Exit");
            String ch = ask("Choose: ");
            if (ch.equals("1")) add(ask("Name: "), ask("Phone: "), ask("Email: "));
            else if (ch.equals("2")) { if (list.isEmpty()) System.out.println("No contacts."); else inorder(root); }
            else if (ch.equals("3")) { if (!searchName(root, ask("Name: "))) System.out.println("Contact not found."); }
            else if (ch.equals("4")) { Contact c = phoneMap.get(ask("Phone: ")); System.out.println(c == null ? "Contact not found." : c); }
            else if (ch.equals("5")) update();
            else if (ch.equals("6")) delete();
            else if (ch.equals("7")) sort(ask("1.Ascending 2.Descending: ").equals("2"));
            else if (ch.equals("8")) { save(); System.out.println("Saved. Bye!"); break; }
            else System.out.println("Invalid choice.");
        }
    }
                                              }
