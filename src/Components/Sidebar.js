// client/src/Components/Sidebar.js
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const Sidebar = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                {/* Sidebar */}
                <div className="col-md-3 sidebar">
                    <h3>Restaurant Dashboard</h3>
                    <div className="table-container p-3">
                        <table className="table table-striped table-dark">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Item Name</th>
                                    <th>Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>Pizza</td>
                                    <td>$10</td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>Burger</td>
                                    <td>$8</td>
                                </tr>
                                {/* Add more rows as needed */}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Main Content */}
                <div className="col-md-9">
                    {/* Navbar */}
                    <nav className="navbar navbar-expand-lg navbar-light bg-light mb-3">
                        <div className="container-fluid">
                            <a className="navbar-brand" href="#">Restaurant</a>
                            <div className="dropdown ms-auto">
                                <button className="btn btn-outline-secondary dropdown-toggle" type="button" id="configDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                    Configure
                                </button>
                                <ul className="dropdown-menu" aria-labelledby="configDropdown">
                                    <li><a className="dropdown-item" href="#">Update Menu</a></li>
                                    <li><a className="dropdown-item" href="#">Update Profile</a></li>
                                    <li><a className="dropdown-item" href="#">Logout</a></li>
                                </ul>
                            </div>
                        </div>
                    </nav>

                    {/* Details Section */}
                    <div className="details-section">
                        <h4>Details</h4>
                        <p>Select an item from the table to view details here.</p>
                        <button className="btn btn-primary mt-3">Add Item</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Sidebar;