import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'; // Import FontAwesome
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons'; // Import icons

const Menu = () => {
    const [menuItems, setMenuItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [newMenuItem, setNewMenuItem] = useState({
        name: '',
        price: '',
        description: '',
        imageUrl: ''
    });
    const [editMenuItem, setEditMenuItem] = useState(null);

    const fetchMenuItems = async () => {
        try {
            const response = await axios.get('http://localhost:8081/menu/items');
            setMenuItems(response.data);
        } catch (error) {
            toast.error('Error fetching menu items');
        }
    };

    const handleSearch = (e) => {
        setSearchQuery(e.target.value);
    };

    const filteredMenuItems = menuItems.filter(item =>
        item.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const handleChange = (e) => {
        setNewMenuItem({ ...newMenuItem, [e.target.name]: e.target.value });
    };

    const handleAddMenuItem = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8081/menu/add', newMenuItem);
            toast.success('Menu item added successfully');
            fetchMenuItems();
            setNewMenuItem({
                name: '',
                price: '',
                description: '',
                imageUrl: ''
            });
        } catch (error) {
            toast.error('Error adding menu item');
        }
    };

    const handleDeleteMenuItem = async (name) => {
        try {
            await axios.delete(`http://localhost:8081/menu/delete/${name}`);
            toast.success('Menu item deleted successfully');
            fetchMenuItems();
        } catch (error) {
            toast.error('Error deleting menu item');
        }
    };

    const handleEditMenuItem = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8081/menu/update/${editMenuItem.name}`, editMenuItem);
            toast.success('Menu item updated successfully');
            fetchMenuItems();
            setEditMenuItem(null);
        } catch (error) {
            toast.error('Error updating menu item');
        }
    };

    const handleEditChange = (e) => {
        setEditMenuItem({ ...editMenuItem, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        fetchMenuItems();
    }, []);

    return (
        <div className="container mt-5">
            <h1 className="text-center mb-4">Menu</h1>
            <div className="row mb-4">
                <div className="col-md-6 mb-3">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search by dish name"
                        value={searchQuery}
                        onChange={handleSearch}
                    />
                </div>
                <div className="col-md-6">
                    <h2>Add New Menu Item</h2>
                    <form onSubmit={handleAddMenuItem}>
                        <div className="row">
                            <div className="col-md-6">
                                <div className="form-group">
                                    <input
                                        type="text"
                                        name="name"
                                        className="form-control"
                                        placeholder="Dish Name"
                                        value={newMenuItem.name}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <input
                                        type="number"
                                        name="price"
                                        className="form-control"
                                        placeholder="Dish Price"
                                        value={newMenuItem.price}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="form-group">
                                    <textarea
                                        name="description"
                                        className="form-control"
                                        placeholder="Dish Description"
                                        value={newMenuItem.description}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <input
                                        type="text"
                                        name="imageUrl"
                                        className="form-control"
                                        placeholder="Dish Image URL (optional)"
                                        value={newMenuItem.imageUrl}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                        </div>
                        <button type="submit" className="btn btn-success">Add Dish</button>
                    </form>
                </div>
            </div>
            <div>
                <h2>Menu Items</h2>
                <div className="row">
                    {filteredMenuItems.length > 0 ? (
                        filteredMenuItems.map((item) => (
                            <div key={item.id} className="col-md-4 mb-3">
                                <div className="card">
                                    <div className="card-body">
                                        <h3>{item.name}</h3>
                                        <p>Price: {item.price} INR</p>
                                        <p>Description: {item.description}</p>
                                        {item.imageUrl && <img src={item.imageUrl} alt={item.name} style={{ width: '100px' }} />}
                                        <div className="d-flex justify-content-between mt-2">
                                            <button className="btn btn-danger btn-sm" onClick={() => handleDeleteMenuItem(item.name)}>
                                                <FontAwesomeIcon icon={faTrash} />
                                            </button>
                                            <button className="btn btn-primary btn-sm" onClick={() => setEditMenuItem(item)}>
                                                <FontAwesomeIcon icon={faEdit} />
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>No menu items found</p>
                    )}
                </div>
            </div>
            {editMenuItem && (
                <div>
                    <h2>Edit Menu Item</h2>
                    <form onSubmit={handleEditMenuItem}>
                        <div className="form-group">
                            <input
                                type="text"
                                name="name"
                                className="form-control"
                                placeholder="Dish Name"
                                value={editMenuItem.name}
                                onChange={handleEditChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="number"
                                name="price"
                                className="form-control"
                                placeholder="Dish Price"
                                value={editMenuItem.price}
                                onChange={handleEditChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <textarea
                                name="description"
                                className="form-control"
                                placeholder="Dish Description"
                                value={editMenuItem.description}
                                onChange={handleEditChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="imageUrl"
                                className="form-control"
                                placeholder="Dish Image URL (optional)"
                                value={editMenuItem.imageUrl}
                                onChange={handleEditChange}
                            />
                        </div>
                        <button type="submit" className="btn btn-success">Update Dish</button>
                        <button type="button" className="btn btn-secondary" onClick={() => setEditMenuItem(null)}>Cancel</button>
                    </form>
                </div>
            )}
        </div>
    );
};

export default Menu;