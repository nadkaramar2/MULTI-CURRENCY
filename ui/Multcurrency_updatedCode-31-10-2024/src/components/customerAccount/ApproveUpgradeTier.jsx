import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { NavLink } from "react-router-dom";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";

export default function ApproveUpgradeTier() {
  const [tier, setTier] = useState("");
  const [disco, setDisco] = useState([]);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    handleSubmit();
    setError("");
  }, []);
  const handleSubmit = async () => {
    // event.preventDefault();
    if (tier === "" || tier.length === 0) {
      setError(true);
      // swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(
          `approve_upgrade_tier/getUpgradeTirType`,
          {
            strTierType: tier,
          }
        );
        if (response.data.code === "S0000") {
          setDisco(response.data.approveUpgradeList);
          handleShowSuccess(response.data.message);
          setCheckpoint(1);
          setError("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setTier("");
    setDisco([]);
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Approve Upgrade Tier
              </p>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[32rem]">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-4  md:grid-cols-3 px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Select Tier Type
                    </label>
                    <select
                      id="tier"
                      name="tier"
                      value={tier}
                      onChange={(e) => setTier(e.target.value)}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      required
                    >
                      <option value="">Select Tier</option>
                      <option value="all">ALL</option>
                      <option value="tier2">Tier 2</option>
                      <option value="tier3">Tier 3</option>
                    </select>
                    {error && tier.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Tier!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="my-2">
                    <button
                      title="Click Search Button"
                      type="button"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-4 mx-2 my-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                    >
                      <MagnifyingGlassIcon className="h-3 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-4 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                    >
                      <XMarkIcon className="h-3 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div className=" flex justify-end   border border-transparent bg-blue-100  text-sm  font-medium text-white ">
          <div className=" flex justify-end  rounded-md border border-transparent  px-5 mx-4 text-xs  font-medium text-white ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Data"
                type="text"
                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />
              {""}
              <button
                type="submit"
                className="absolute right-0 top-0 mt-5 mr-4 px-4  "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
        {/* table */}
        <div className="bg-white overflow-x-auto relative shadow-md  ">
          <div className="overflow-x-auto relative shadow-md  ">
            <table className="w-full text-xs text-left text-blue-100 dark:text-blue-100">
              <thead className="text-xs text-black uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2.5 px-6">
                    CUST ID
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    CUST NAME
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    TIER TYPE
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    LINK
                  </th>
                </tr>
              </thead>
              <tbody>
                {disco.map(({ strCustId, strCustName, strTierType }) => (
                  <tr
                    key={uuidv4()}
                    className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                  >
                    <td className="px-6 py-2.5 whitespace-nowrap">
                      <span className="text-xs font-medium text-gray-900">
                        {strCustId || "-"}
                      </span>
                    </td>
                    <td className="px-6 py-2.5 whitespace-nowrap">
                      <span className="text-xs font-medium text-gray-900">
                        {strCustName || "-"}
                      </span>
                    </td>
                    <td className="px-6 py-2.5 whitespace-nowrap">
                      <span className="text-xs font-medium text-gray-900">
                        {strTierType || "-"}
                      </span>
                    </td>
                    <td className="px-6 py-2.5 whitespace-nowrap">
                      <NavLink
                        title="Click Here to  link"
                        // to="/upgrade-custtier"
                        to={`/upgrade-custtier/${strCustId}/${strTierType}`}
                        className="text-xs font-medium text-blue-700"
                      >
                        Click here to Upgrade
                      </NavLink>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      </div>
      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
